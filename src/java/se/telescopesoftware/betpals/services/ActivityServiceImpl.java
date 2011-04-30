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
	private Integer defaultItemsPerPage;
	

	private static Logger logger = Logger.getLogger(ActivityServiceImpl.class);

    
    public void setActivityRepository(ActivityRepository activityRepository) {
    	this.activityRepository = activityRepository;
    }
    
    public Integer getDefaultItemsPerPage() {
    	return defaultItemsPerPage;
    }
    
    public void setDefaultItemsPerPage(Integer defaultItemsPerPage) {
    	this.defaultItemsPerPage = defaultItemsPerPage;
    }
	
	public void saveActivity(Activity activity) {
		activityRepository.saveActivity(activity);
	}

	public Collection<Activity> getActivitiesForUserProfile(UserProfile userProfile, Integer pageNumber, Integer itemsPerPage) {
		List<Long> ownerIds = new ArrayList<Long>();
		ownerIds.addAll(userProfile.getFriendsIdSet());
		ownerIds.add(userProfile.getUserId());
		
		return activityRepository.loadActivitiesForOwnerIds(
				ownerIds, 
				pageNumber != null ? pageNumber : 0, 
				itemsPerPage != null ? itemsPerPage : defaultItemsPerPage);
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

	public void deleteActivityComment(Long commentId, Long userId) {
		ActivityComment comment = activityRepository.loadActivityComment(commentId);
		if (comment.checkOwnership(userId)) {
			logger.info("Comment deleted by owner: " + comment.toString());
			activityRepository.deleteActivityComment(comment);
		}
	}

	public void deleteActivityLike(Long likeId, Long userId) {
		ActivityLike like = activityRepository.loadActivityLike(likeId);
		if (like.checkOwnership(userId)) {
			logger.info("Like is deleted by owner: " + like.toString());
			activityRepository.deleteActivityLike(like);
		}
	}

	public void deleteActivity(Long activityId, Long userId) {
		Activity activity = activityRepository.loadActivity(activityId);
		if (activity.checkOwnership(userId) 
				&& !activity.hasComments()
				&& !activity.hasLikes()) {
			activityRepository.deleteActivity(activity);
		}
	}

	public Activity getActivity(Long activityId) {
		return activityRepository.loadActivity(activityId);
	}

	public Integer getActivitiesPageCountForUserProfile(UserProfile userProfile, Integer itemsPerPage) {
		List<Long> ownerIds = new ArrayList<Long>();
		ownerIds.addAll(userProfile.getFriendsIdSet());
		ownerIds.add(userProfile.getUserId());

		Integer totalActivityCount = activityRepository.getActivitiesCountForUserProfile(ownerIds);
		if (itemsPerPage == null) {
			itemsPerPage = defaultItemsPerPage;
		}
		
        if (totalActivityCount != null) {
        	Integer result = new Integer(totalActivityCount.intValue() / itemsPerPage.intValue());
        	if ((result.intValue() * itemsPerPage.intValue()) < totalActivityCount.intValue()) {
        		result += 1;
        	}
            return result;
        }

        return new Integer(0);
	}

}
