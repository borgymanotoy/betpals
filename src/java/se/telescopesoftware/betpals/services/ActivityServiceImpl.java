package se.telescopesoftware.betpals.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityComment;
import se.telescopesoftware.betpals.domain.ActivityLike;
import se.telescopesoftware.betpals.domain.ActivityType;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.repository.ActivityRepository;

@Service
@Transactional(readOnly = true)
public class ActivityServiceImpl implements ActivityService {

	private ActivityRepository activityRepository;
	private SiteConfigurationService siteConfigurationService;

	
	private static Logger logger = Logger.getLogger(ActivityServiceImpl.class);

    
    @Autowired
    public void setActivityRepository(ActivityRepository activityRepository) {
    	this.activityRepository = activityRepository;
    }
    
    @Autowired
    public void setSiteConfigurationService(SiteConfigurationService siteConfigurationService) {
    	this.siteConfigurationService = siteConfigurationService;
    }

    
	@Transactional(readOnly = false)
	public void saveActivity(Activity activity) {
		logger.info("Saving " + activity);
		activityRepository.saveActivity(activity);
	}

	public Collection<Activity> getActivitiesForUserProfile(UserProfile userProfile, Integer pageNumber, Integer itemsPerPage) {
		List<Long> ownerIds = new ArrayList<Long>();
		ownerIds.addAll(userProfile.getFriendsIdSet());
		ownerIds.add(userProfile.getUserId());
		int activitiesPerPage = new Integer(siteConfigurationService.getParameterValue("activities.per.page", "10")).intValue();

		return activityRepository.loadActivitiesForOwnerIds(
				ownerIds, 
				pageNumber != null ? pageNumber : 0, 
				itemsPerPage != null ? itemsPerPage : activitiesPerPage, ActivityType.USER);
	}

	@Transactional(readOnly = false)
	public void addActivityComment(ActivityComment comment) {
		logger.info("Saving " + comment);
		activityRepository.saveActivityComment(comment);
	}

	@Transactional(readOnly = false)
	public void addActivityLike(ActivityLike like) {
		logger.info("Saving " + like);
		activityRepository.saveActivityLike(like);
	}

	public Collection<ActivityComment> getActivityComments(Long activityId) {
		return activityRepository.loadActivityComments(activityId);
	}

	public Collection<ActivityLike> getActivityLikes(Long activityId) {
		return activityRepository.loadActivityLikes(activityId);
	}

	@Transactional(readOnly = false)
	public void deleteActivityComment(Long commentId, Long userId) {
		ActivityComment comment = activityRepository.loadActivityComment(commentId);
		if (comment.checkOwnership(userId)) {
			logger.info("Deleting by owner " + comment);
			activityRepository.deleteActivityComment(comment);
		}
	}

	@Transactional(readOnly = false)
	public void deleteActivityLike(Long likeId, Long userId) {
		ActivityLike like = activityRepository.loadActivityLike(likeId);
		if (like.checkOwnership(userId)) {
			logger.info("Deleting by owner " + like);
			activityRepository.deleteActivityLike(like);
		}
	}

	@Transactional(readOnly = false)
	public void deleteActivity(Long activityId, Long userId) {
		Activity activity = activityRepository.loadActivity(activityId);
		if (activity != null) {
			if (activity.checkOwnership(userId)) {
				logger.info("Note: This activity has some comments and likes which will also be deleted. ");

				logger.info("Deleting likes " + activity);
				Collection<ActivityLike> likes = activity.getLikes();
				for (ActivityLike like : likes) {
					activityRepository.deleteActivityLike(like);
				}

				logger.info("Deleting comments" + activity);
				Collection<ActivityComment> comments = activity.getComments();
				for (ActivityComment comment : comments) {
					activityRepository.deleteActivityComment(comment);
				}

				logger.info("Deleting activity by owner " + activity);
				activityRepository.deleteActivity(activity);
			}
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
			itemsPerPage = new Integer(siteConfigurationService.getParameterValue("activities.per.page", "10")).intValue();
		}
		
        if (totalActivityCount != null) {
        	Integer result = new Integer(totalActivityCount.intValue() / itemsPerPage.intValue());
        	if (result.intValue() != 0 && (result.intValue() * itemsPerPage.intValue()) < totalActivityCount.intValue()) {
        		result += 1;
        	}
            return result;
        }

        return new Integer(0);
	}

	public Collection<Activity> getActivitiesForExtensionIdAndType(Long extensionId, Integer pageNumber, Integer itemsPerPage, ActivityType activityType) {
		int activitiesPerPage = new Integer(siteConfigurationService.getParameterValue("activities.per.page", "10")).intValue();
		return activityRepository.loadActivitiesForExtensionIdAndType(extensionId,
				pageNumber != null ? pageNumber : 0, 
				itemsPerPage != null ? itemsPerPage : activitiesPerPage, activityType);
	}

	public Integer getActivitiesPageCountForExtensionIdAndType(Long extensionId,
			ActivityType activityType, Integer itemsPerPage) {

		Integer totalActivityCount = activityRepository.getActivitiesCountForExtensionIdAndType(extensionId, activityType);
		if (itemsPerPage == null) {
			itemsPerPage = new Integer(siteConfigurationService.getParameterValue("activities.per.page", "10")).intValue();
		}
		
        if (totalActivityCount != null) {
        	Integer result = new Integer(totalActivityCount.intValue() / itemsPerPage.intValue());
        	if (result.intValue() != 0 && (result.intValue() * itemsPerPage.intValue()) < totalActivityCount.intValue()) {
        		result += 1;
        	}
            return result;
        }

        return new Integer(0);
	}

	public ActivityLike getActivityLike(Long activityLikeId) {
		return activityRepository.loadActivityLike(activityLikeId);
	}

	public ActivityComment getActivityComment(Long activityCommentId) {
		return activityRepository.loadActivityComment(activityCommentId);
	}

}
