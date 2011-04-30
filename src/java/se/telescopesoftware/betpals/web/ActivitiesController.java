package se.telescopesoftware.betpals.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityComment;
import se.telescopesoftware.betpals.domain.ActivityLike;
import se.telescopesoftware.betpals.domain.ActivityType;
import se.telescopesoftware.betpals.services.ActivityService;

@Controller
public class ActivitiesController extends AbstractPalsController {

    private ActivityService activityService;

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @RequestMapping(value="/activities")
    public String post(@RequestParam("message") String message) {
    	Activity activity = new Activity(getUserProfile(), message, ActivityType.MESSAGE);
    	activityService.saveActivity(activity);
    	
    	return "userHomepageAction";
    }

    @RequestMapping(value="/activitycomment")
    public String postComment(@RequestParam("message") String message, @RequestParam("activityId") Long activityId) {
    	Activity activity = activityService.getActivity(activityId);
    	if (activity != null) {
    		ActivityComment comment = new ActivityComment(getUserProfile(), message, activity);
    		activity.addComment(comment);
    		activityService.saveActivity(activity);
    	}    	

    	return "userHomepageAction";
    }
    
    @RequestMapping(value="/activitylike")
    public String postLike(@RequestParam("activityId") Long activityId) {
    	Activity activity = activityService.getActivity(activityId);
    	if (activity != null) {
    		ActivityLike like = new ActivityLike(getUserProfile(), activity);
    		activity.addLike(like);
    		activityService.saveActivity(activity);
    	}
    	
    	return "userHomepageAction";
    }
    
    @RequestMapping(value="/removelike")
    public String removeLike(@RequestParam("likeId") Long likeId) {
    	activityService.deleteActivityLike(likeId, getUserId());
    	
    	return "userHomepageAction";
    }
    
    @RequestMapping(value="/removecomment")
    public String removeComment(@RequestParam("commentId") Long commentId) {
    	activityService.deleteActivityComment(commentId, getUserId());
    	
    	return "userHomepageAction";
    }
    
    @RequestMapping(value="/removeactivity")
    public String removeActivity(@RequestParam("activityId") Long activityId) {
    	activityService.deleteActivity(activityId, getUserId());
    	
    	return "userHomepageAction";
    }
    
}
