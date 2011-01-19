package se.telescopesoftware.betpals.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import se.telescopesoftware.betpals.domain.Alternative;
import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.Event;
import se.telescopesoftware.betpals.domain.Invitation;

public class HibernateCompetitionRepositoryImpl extends HibernateDaoSupport
		implements CompetitionRepository {

	public Competition storeCompetition(Competition competition) {
		return getHibernateTemplate().merge(competition);
	}

	public Collection<Competition> loadActiveCompetitionsByUser(Long userId) {
		List<Competition> result = new ArrayList<Competition>();
		Session session = getSession();
    	Query query = session.createQuery("from Competition c where c.ownerId = :ownerId and c.deadline >= :currentDate order by c.created desc");
    	query.setDate("currentDate", new Date());
    	query.setLong("ownerId", userId);
    	result = query.list();
    	session.close();

    	return result;
	}
	
	public Integer getActiveCompetitionsByUserCount(Long userId) {
		return DataAccessUtils.intResult(getHibernateTemplate().findByNamedParam(
				"select count(*) from Competition c where c.ownerId = :ownerId and c.deadline >= :currentDate", 
				new String[] {"ownerId", "currentDate"}, 
				new Object[] {userId, new Date()}));
	}

	public void storeBet(Bet bet) {
		getHibernateTemplate().saveOrUpdate(bet);
	}

	public Collection<Bet> loadActiveBetsByUser(Long userId) {
		return getHibernateTemplate().findByNamedParam("from Bet b where b.ownerId = :ownerId", "ownerId", userId);
	}

	public Collection<Bet> loadActiveBetsByUserAndAccount(Long userId, Long accountId) {
		return getHibernateTemplate().findByNamedParam("from Bet b where b.ownerId = :ownerId and accountId = :accountId", 
				new String[] {"ownerId", "accountId"},
				new Object [] {userId, accountId});
	}

	public void storeInvitation(Invitation invitation) {
		getHibernateTemplate().saveOrUpdate(invitation);
	}

	public Integer getInvitationsForUserCount(Long userId) {
		return DataAccessUtils.intResult(getHibernateTemplate().findByNamedParam("select count(*) from Invitation i where i.inviteeId = :userId", "userId", userId));
	}

	public Collection<Invitation> loadInvitationsForUser(Long userId) {
		return getHibernateTemplate().findByNamedParam("from Invitation i where i.inviteeId = :userId", "userId", userId);
	}

	public Invitation loadInvitationById(Long id) {
		return getHibernateTemplate().get(Invitation.class, id);
	}

	public Competition loadCompetitionById(Long id) {
		return getHibernateTemplate().get(Competition.class, id);
	}

	public void deleteInvitation(Invitation invitation) {
		getHibernateTemplate().delete(invitation);
	}

	public Event loadEventById(Long id) {
		return getHibernateTemplate().get(Event.class, id);
	}

	public Alternative storeAlternative(Alternative alternative) {
		return getHibernateTemplate().merge(alternative);
	}

	public Event storeEvent(Event event) {
		return getHibernateTemplate().merge(event);
	}

	public void deleteCompetition(Competition competition) {
		getHibernateTemplate().delete(competition);
	}

	public Collection<Bet> loadActiveBetsBySelectionId(Long selectionId) {
		return getHibernateTemplate().findByNamedParam("from Bet b where b.selectionId = :selectionId", "selectionId", selectionId);
	}

	public void deleteBet(Bet bet) {
		getHibernateTemplate().delete(bet);
	}

}
