package se.telescopesoftware.betpals.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityComment;
import se.telescopesoftware.betpals.domain.ActivityLike;
import se.telescopesoftware.betpals.domain.ActivityType;
import se.telescopesoftware.betpals.domain.UserProfile;
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
    	UserProfile userProfile = getUserProfile();
    	
    	Activity activity = new Activity();
    	activity.setCreated(new Date());
    	activity.setOwnerId(getUserId());
    	activity.setOwnerName(userProfile.getFullName());
    	activity.setActivityType(ActivityType.MESSAGE);
    	activity.setMessage(message);
    	
    	activityService.addActivity(activity);
    	
    	return "userHomepageAction";
    }

    @RequestMapping(value="/activitycomment")
    public String postComment(@RequestParam("message") String message, @RequestParam("activityId") Long activityId) {
    	UserProfile userProfile = getUserProfile();
    	
    	ActivityComment comment = new ActivityComment();
    	comment.setCreated(new Date());
    	comment.setOwnerId(getUserId());
    	comment.setOwnerName(userProfile.getFullName());
    	comment.setMessage(message);
    	comment.setActivityId(activityId);
    	
    	activityService.addActivityComment(comment);
    	
    	return "userHomepageAction";
    }
    
    @RequestMapping(value="/activitylike")
    public String postLike(@RequestParam("activityId") Long activityId) {
    	UserProfile userProfile = getUserProfile();
    	
    	ActivityLike like = new ActivityLike();
    	like.setCreated(new Date());
    	like.setOwnerId(getUserId());
    	like.setOwnerName(userProfile.getFullName());
    	like.setActivityId(activityId);
    	
    	activityService.addActivityLike(like);
    	
    	return "userHomepageAction";
    }
    
}
