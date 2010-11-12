package se.telescopesoftware.betpals.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityComment;
import se.telescopesoftware.betpals.domain.ActivityLike;

public class HibernateActivityRepositoryImpl extends HibernateDaoSupport
		implements ActivityRepository {


	public void saveActivity(Activity activity) {
		getHibernateTemplate().saveOrUpdate(activity);
	}

	public Collection<Activity> loadActivitiesForOwnerIds(Collection<Long> ownerIds) {
    	List<Activity> result = new ArrayList<Activity>();
		Session session = getSession();
    	Query query = session.createQuery("from Activity a where a.ownerId in (:ownerIds)");
    	query.setParameterList("ownerIds", ownerIds);
    	result = query.list();
    	session.close();
    	
    	for(Activity activity : result) {
    		activity.setComments(loadActivityComments(activity.getId()));
    		activity.setLikes(loadActivityLikes(activity.getId()));
    	}
    	
    	return result;
	}

	public void saveActivityComment(ActivityComment comment) {
		getHibernateTemplate().saveOrUpdate(comment);
	}

	public void saveActivityLike(ActivityLike like) {
		getHibernateTemplate().saveOrUpdate(like);
	}

	public Collection<ActivityComment> loadActivityComments(Long activityId) {
		return getHibernateTemplate().find("from ActivityComment ac where ac.activityId = ?", activityId);
	}

	public Collection<ActivityLike> loadActivityLikes(Long activityId) {
		return getHibernateTemplate().find("from ActivityLike al where al.activityId = ?", activityId);
	}
}
