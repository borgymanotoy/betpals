package se.telescopesoftware.betpals.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityComment;
import se.telescopesoftware.betpals.domain.ActivityLike;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.repository.ActivityRepository;

public class ActivityServiceImpl implements ActivityService {

	private ActivityRepository activityRepository;
	
    private static Logger logger = Logger.getLogger(ActivityServiceImpl.class);

    
    public void setActivityRepository(ActivityRepository activityRepository) {
    	this.activityRepository = activityRepository;
    }
    
	
	public void addActivity(Activity activity) {
		activityRepository.saveActivity(activity);
	}

	public Collection<Activity> getActivitiesForUserProfile(UserProfile userProfile) {
		List<Long> ownerIds = new ArrayList<Long>();
		ownerIds.addAll(userProfile.getFriendsIdSet());
		ownerIds.add(userProfile.getUserId());
		
		return activityRepository.loadActivitiesForOwnerIds(ownerIds);
	}

	public void addActivityComment(ActivityComment comment) {
		activityRepository.saveActivityComment(comment);
	}

	public void addActivityLike(ActivityLike like) {
		activityRepository.saveActivityLike(like);
	}

	public Collection<ActivityComment> getActivityComments(Long activityId) {
		return activityRepository.loadActivityComments(activityId);
	}

	public Collection<ActivityLike> getActivityLikes(Long activityId) {
		return activityRepository.loadActivityLikes(activityId);
	}

}
