package se.telescopesoftware.betpals.repository;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityComment;
import se.telescopesoftware.betpals.domain.ActivityLike;
import se.telescopesoftware.betpals.domain.ActivityType;

@Repository
public class HibernateActivityRepositoryImpl implements ActivityRepository {

	private SessionFactory sessionFactory;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	

	public void saveActivity(Activity activity) {
    	Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(activity);
	}

	@SuppressWarnings("unchecked")
	public Collection<Activity> loadActivitiesForOwnerIds(Collection<Long> ownerIds, Integer pageNumber, Integer itemsPerPage, ActivityType activityType) {
    	Session session = sessionFactory.getCurrentSession();
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
    	
    	return query.list();
	}
	
	public void saveActivityComment(ActivityComment comment) {
    	Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(comment);
	}

	public void saveActivityLike(ActivityLike like) {
    	Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(like);
	}

	@SuppressWarnings("unchecked")
	public Collection<ActivityComment> loadActivityComments(Long activityId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from ActivityComment ac where ac.activity.id = :activityId");
    	query.setLong("activityId", activityId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public Collection<ActivityLike> loadActivityLikes(Long activityId) {
    	Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from ActivityLike al where al.activity.id = :activityId");
    	query.setLong("activityId", activityId);
		return query.list();
	}

	public ActivityComment loadActivityComment(Long commentId) {
    	Session session = sessionFactory.getCurrentSession();
		return (ActivityComment) session.get(ActivityComment.class, commentId);
	}

	public ActivityLike loadActivityLike(Long likeId) {
    	Session session = sessionFactory.getCurrentSession();
		return (ActivityLike) session.get(ActivityLike.class, likeId);
	}

	public void deleteActivityComment(ActivityComment comment) {
    	Session session = sessionFactory.getCurrentSession();
		session.delete(comment);
	}

	public void deleteActivityLike(ActivityLike like) {
    	Session session = sessionFactory.getCurrentSession();
		session.delete(like);
	}

	public void deleteActivity(Activity activity) {
    	Session session = sessionFactory.getCurrentSession();
		session.delete(activity);
	}

	public Activity loadActivity(Long activityId) {
    	Session session = sessionFactory.getCurrentSession();
		return (Activity) session.get(Activity.class, activityId);
	}

	public Integer getActivitiesCountForUserProfile(Collection<Long> ownerIds) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("select count(*) from Activity a where a.ownerId in (:ownerIds) and a.activityType = :activityType");
    	query.setParameterList("ownerIds", ownerIds);
    	query.setParameter("activityType", ActivityType.USER);
    	return DataAccessUtils.intResult(query.list());
	}

	@SuppressWarnings("unchecked")
	public Collection<Activity> loadActivitiesForExtensionIdAndType(
			Long extensionId, Integer pageNumber, Integer itemsPerPage,
			ActivityType activityType) {
    	Session session = sessionFactory.getCurrentSession();
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
    	
    	return query.list();
	}

	public Integer getActivitiesCountForExtensionIdAndType(Long extensionId, ActivityType activityType) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("select count(*) from Activity a where a.extensionId = :extensionId and a.activityType = :activityType");
    	query.setLong("extensionId", extensionId);
    	query.setParameter("activityType", activityType);
    	return DataAccessUtils.intResult(query.list());
	}
}
