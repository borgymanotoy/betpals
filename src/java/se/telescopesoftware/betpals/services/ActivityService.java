package se.telescopesoftware.betpals.services;

import java.util.Collection;

import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityComment;
import se.telescopesoftware.betpals.domain.ActivityLike;
import se.telescopesoftware.betpals.domain.UserProfile;

/**
 * Functionality related to user wall.
 * 
 *
 */
public interface ActivityService {

	void saveActivity(Activity activity);
	
	void addActivityComment(ActivityComment comment);
	
	void addActivityLike(ActivityLike like);
	
	Collection<Activity> getActivitiesForUserProfile(UserProfile userProfile, Integer pageNumber, Integer itemsPerPage);
	
	Collection<ActivityComment> getActivityComments(Long activityId);
	
	Collection<ActivityLike> getActivityLikes(Long activityId);

	Activity getActivity(Long activityId);
	
	/**
	 * Delete activity comment from the wall.
	 * 
	 * @param commentId
	 * @param userId current user id to check ownership
	 */
	void deleteActivityComment(Long commentId, Long userId);
	
	/**
	 * Delete like entry from the wall.
	 * 
	 * @param likeId
	 * @param userId current user id to check ownership
	 */
	void deleteActivityLike(Long likeId, Long userId);
	
	/**
	 * Delete activity entry from user wall. 
	 * 
	 * @param activityId
	 * @param userId
	 */
	void deleteActivity(Long activityId, Long userId);
	
	Integer getActivitiesPageCountForUserProfile(UserProfile userProfile, Integer itemsPerPage);
	
}
