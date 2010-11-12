package se.telescopesoftware.betpals.services;

import java.util.Collection;

import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityComment;
import se.telescopesoftware.betpals.domain.ActivityLike;
import se.telescopesoftware.betpals.domain.UserProfile;

public interface ActivityService {

	void addActivity(Activity activity);
	
	void addActivityComment(ActivityComment comment);
	
	void addActivityLike(ActivityLike like);
	
	Collection<Activity> getActivitiesForUserProfile(UserProfile userProfile);
	
	Collection<ActivityComment> getActivityComments(Long activityId);
	
	Collection<ActivityLike> getActivityLikes(Long activityId);
}
