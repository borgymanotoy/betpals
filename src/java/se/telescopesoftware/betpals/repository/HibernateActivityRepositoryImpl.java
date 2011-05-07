package se.telescopesoftware.betpals.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityComment;
import se.telescopesoftware.betpals.domain.ActivityLike;
import se.telescopesoftware.betpals.domain.ActivityType;

public class HibernateActivityRepositoryImpl extends HibernateDaoSupport
		implements ActivityRepository {


	public void saveActivity(Activity activity) {
		getHibernateTemplate().saveOrUpdate(activity);
	}

	@SuppressWarnings("unchecked")
	public Collection<Activity> loadActivitiesForOwnerIds(Collection<Long> ownerIds, Integer pageNumber, Integer itemsPerPage, ActivityType activityType) {
    	List<Activity> result = new ArrayList<Activity>();
		Session session = getSession();
    	Query query = session.createQuery("from Activity a where a.ownerId in (:ownerIds) and a.activityType = :activityType order by a.created desc");
    	query.setParameterList("ownerIds", ownerIds);
    	query.setParameter("activityType", activityType);
    	
    	int offset = 0;
        int resultsPerPage = itemsPerPage.intValue();
        if (pageNumber.intValue() > 0) {
            offset = pageNumber.intValue() * resultsPerPage;
        }

        query.setFirstResult(offset);
        query.setMaxResults(resultsPerPage);
    	
    	result = query.list();
    	session.close();
    	
//    	for(Activity activity : result) {
//    		activity.setComments(loadActivityComments(activity.getId()));
//    		activity.setLikes(loadActivityLikes(activity.getId()));
//    	}
    	
    	return result;
	}
	
	public void saveActivityComment(ActivityComment comment) {
		getHibernateTemplate().saveOrUpdate(comment);
	}

	public void saveActivityLike(ActivityLike like) {
		getHibernateTemplate().saveOrUpdate(like);
	}

	@SuppressWarnings("unchecked")
	public Collection<ActivityComment> loadActivityComments(Long activityId) {
		return getHibernateTemplate().find("from ActivityComment ac where ac.activity.id = ?", activityId);
	}

	@SuppressWarnings("unchecked")
	public Collection<ActivityLike> loadActivityLikes(Long activityId) {
		return getHibernateTemplate().find("from ActivityLike al where al.activity.id = ?", activityId);
	}

	public ActivityComment loadActivityComment(Long commentId) {
		return getHibernateTemplate().get(ActivityComment.class, commentId);
	}

	public ActivityLike loadActivityLike(Long likeId) {
		return getHibernateTemplate().get(ActivityLike.class, likeId);
	}

	public void deleteActivityComment(ActivityComment comment) {
		getHibernateTemplate().delete(comment);
	}

	public void deleteActivityLike(ActivityLike like) {
		getHibernateTemplate().delete(like);
	}

	public void deleteActivity(Activity activity) {
		getHibernateTemplate().delete(activity);
	}

	public Activity loadActivity(Long activityId) {
		return getHibernateTemplate().get(Activity.class, activityId);
	}

	public Integer getActivitiesCountForUserProfile(Collection<Long> ownerIds) {
    	return DataAccessUtils.intResult(
    			getHibernateTemplate().findByNamedParam("select count(*) from Activity a where a.ownerId in (:ownerIds) and a.activityType = :activityType",
    					new String[] {"ownerIds", "activityType"},
    					new Object[] { ownerIds, ActivityType.USER }) );
	}

	@SuppressWarnings("unchecked")
	public Collection<Activity> loadActivitiesForExtensionIdAndType(
			Long extensionId, Integer pageNumber, Integer itemsPerPage,
			ActivityType activityType) {
    	List<Activity> result = new ArrayList<Activity>();
		Session session = getSession();
    	Query query = session.createQuery("from Activity a where a.extensionId = :extensionId and a.activityType = :activityType order by a.created desc");
    	query.setParameter("extensionId", extensionId);
    	query.setParameter("activityType", activityType);
    	
    	int offset = 0;
        int resultsPerPage = itemsPerPage.intValue();
        if (pageNumber.intValue() > 0) {
            offset = pageNumber.intValue() * resultsPerPage;
        }

        query.setFirstResult(offset);
        query.setMaxResults(resultsPerPage);
    	
    	result = query.list();
    	session.close();
    	
    	return result;
	}

	public Integer getActivitiesCountForExtensionIdAndType(Long extensionId, ActivityType activityType) {
    	return DataAccessUtils.intResult(
    			getHibernateTemplate().findByNamedParam("select count(*) from Activity a where a.extensionId = :extensionId and a.activityType = :activityType",
    					new String[] {"extensionId", "activityType"},
    					new Object[] { extensionId, activityType }) );
	}
}
