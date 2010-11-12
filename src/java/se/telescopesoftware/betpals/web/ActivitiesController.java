package se.telescopesoftware.betpals.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityComment;
import se.telescopesoftware.betpals.domain.ActivityLike;
import se.telescopesoftware.betpals.domain.ActivityType;
import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.ActivityService;

@Controller
public class ActivitiesController {

    private ActivityService activityService;

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @RequestMapping(value="/activities", method = RequestMethod.POST)
    public String post(@RequestParam("message") String message) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	User user = (User) authentication.getPrincipal();
    	UserProfile userProfile = user.getUserProfile();
    	
    	Activity activity = new Activity();
    	activity.setCreated(new Date());
    	activity.setOwnerId(user.getId());
    	activity.setOwnerName(userProfile.getFullName());
    	activity.setActivityType(ActivityType.MESSAGE);
    	activity.setMessage(message);
    	
    	activityService.addActivity(activity);
    	
    	return "userHomepageAction";
    }

    @RequestMapping(value="/activitycomment", method = RequestMethod.POST)
    public String postComment(@RequestParam("message") String message, @RequestParam("activityId") Long activityId) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	User user = (User) authentication.getPrincipal();
    	UserProfile userProfile = user.getUserProfile();
    	
    	ActivityComment comment = new ActivityComment();
    	comment.setCreated(new Date());
    	comment.setOwnerId(user.getId());
    	comment.setOwnerName(userProfile.getFullName());
    	comment.setMessage(message);
    	comment.setActivityId(activityId);
    	
    	activityService.addActivityComment(comment);
    	
    	return "userHomepageAction";
    }
    
    @RequestMapping(value="/activitylike", method = RequestMethod.POST)
    public String postLike(@RequestParam("message") String message, @RequestParam("activityId") Long activityId) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	User user = (User) authentication.getPrincipal();
    	UserProfile userProfile = user.getUserProfile();
    	
    	ActivityLike like = new ActivityLike();
    	like.setCreated(new Date());
    	like.setOwnerId(user.getId());
    	like.setOwnerName(userProfile.getFullName());
    	like.setActivityId(activityId);
    	
    	activityService.addActivityLike(like);
    	
    	return "userHomepageAction";
    }
    
}
