package se.telescopesoftware.betpals.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityComment;
import se.telescopesoftware.betpals.domain.ActivityLike;
import se.telescopesoftware.betpals.domain.ActivityType;
import se.telescopesoftware.betpals.domain.Community;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.services.ActivityService;
import se.telescopesoftware.betpals.services.CompetitionService;
import se.telescopesoftware.betpals.services.UserService;

@Controller
public class ActivitiesController extends AbstractPalsController {

    private ActivityService activityService;
    private UserService userService;
	private CompetitionService competitionService;

    
    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Autowired
    public void setUserService(UserService userService) {
    	this.userService = userService;
    }

	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}

    
    @RequestMapping(value="/activities")
    public String post(@RequestParam("message") String message, 
    		@RequestParam(value="communityId", required = false) Long communityId, 
    		@RequestParam(value="competitionId", required = false) Long competitionId, Model model) {
    	if (communityId != null) {
    		Community community = userService.getCommunityById(communityId);
    		if (community.checkMembership(getUserId())) {
    			Activity activity = new Activity(getUserProfile(), community.getId(), community.getName(), message, ActivityType.COMMUNITY);
    			activityService.saveActivity(activity);
    			model.addAttribute("communityId", communityId);
    			return "viewCommunityAction";
    		}
    	} else if (competitionId != null) {
    		Competition competition = competitionService.getCompetitionById(competitionId);
			Activity activity = new Activity(getUserProfile(), competition.getId(), competition.getName(), message, ActivityType.COMPETITION);
			activityService.saveActivity(activity);
			model.addAttribute("competitionId", competitionId);
			return "ongoingCompetitionAction";
    	} else {
    		Activity activity = new Activity(getUserProfile(), message, ActivityType.USER);
    		activityService.saveActivity(activity);
    	}
    	
    	return "userHomepageAction";
    }

    @RequestMapping(value="/activitycomment")
    public String postComment(@RequestParam("message") String message, @RequestParam("activityId") Long activityId, Model model) {
    	Activity activity = activityService.getActivity(activityId);
    	if (activity != null) {
    		ActivityComment comment = new ActivityComment(getUserProfile(), message, activity);
    		activity.addComment(comment);
    		activityService.saveActivity(activity);
    		
    		return getReturnUrlForActivity(activity, model);
    	}    	

    	return "userHomepageAction";
    }
    
    @RequestMapping(value="/activitylike")
    public String postLike(@RequestParam("activityId") Long activityId, Model model) {
    	Activity activity = activityService.getActivity(activityId);
    	if (activity != null) {
    		ActivityLike like = new ActivityLike(getUserProfile(), activity);
    		activity.addLike(like);
    		activityService.saveActivity(activity);
    		return getReturnUrlForActivity(activity, model);
    	}
    	
    	return "userHomepageAction";
    }
    
    @RequestMapping(value="/removelike")
    public String removeLike(@RequestParam("likeId") Long likeId, Model model) {
    	ActivityLike activityLike = activityService.getActivityLike(likeId);
    	Activity activity = activityLike.getActivity();
    	
    	if (activityLike.checkOwnership(getUserId())) {
    		activity.removeLike(activityLike);
    		activityService.saveActivity(activity);
    	}
    	
    	return getReturnUrlForActivity(activity, model);
    }
    
    @RequestMapping(value="/removecomment")
    public String removeComment(@RequestParam("commentId") Long commentId, Model model) {
    	ActivityComment comment = activityService.getActivityComment(commentId);
    	
    	Activity activity = comment.getActivity();
    	if (comment.checkOwnership(getUserId())) {
    		activity.removeComment(comment);
    		activityService.saveActivity(activity);
    	}
    	
    	return getReturnUrlForActivity(activity, model);
    }
    
    @RequestMapping(value="/removeactivity")
    public String removeActivity(@RequestParam("activityId") Long activityId, Model model) {
    	Activity activity = activityService.getActivity(activityId);
    	activityService.deleteActivity(activityId, getUserId());
    	
    	return getReturnUrlForActivity(activity, model);
    }
    
    private String getReturnUrlForActivity(Activity activity, Model model) {
		if (activity.getActivityType() == ActivityType.COMMUNITY) {
			model.addAttribute("communityId", activity.getExtensionId());
			return "viewCommunityAction";
		} else if (activity.getActivityType() == ActivityType.COMPETITION) {
			model.addAttribute("competitionId", activity.getExtensionId());
			return "ongoingCompetitionAction";
		}
    	return "userHomepageAction";
    }
    
}
