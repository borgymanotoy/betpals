package se.telescopesoftware.betpals.repository;

import java.util.Collection;

import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityComment;
import se.telescopesoftware.betpals.domain.ActivityLike;

/**
 * Used to store and retrieve from persistent storage all Activity related information.
 *
 */
public interface ActivityRepository {
	
	void saveActivity(Activity activity);
	
	void saveActivityComment(ActivityComment comment);
	
	void saveActivityLike(ActivityLike like);
	
	Collection<Activity> loadActivitiesForOwnerIds(Collection<Long> ownerIds, Integer pageNumber, Integer itemsPerPage);
	
	Collection<ActivityComment> loadActivityComments(Long activityId);
	
	Collection<ActivityLike> loadActivityLikes(Long activityId);
	
	ActivityComment loadActivityComment(Long commentId);
	
	ActivityLike loadActivityLike(Long likeId);
	
	Activity loadActivity(Long activityId);
	
	void deleteActivityComment(ActivityComment comment);
	
	void deleteActivityLike(ActivityLike like);
	
	void deleteActivity(Activity activity);
	
	Integer getActivitiesCountForUserProfile(Collection<Long> ownerIds);

}
