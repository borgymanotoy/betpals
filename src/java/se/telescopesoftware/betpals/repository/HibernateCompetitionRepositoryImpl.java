package se.telescopesoftware.betpals.repository;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import se.telescopesoftware.betpals.domain.Alternative;
import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.CompetitionStatus;
import se.telescopesoftware.betpals.domain.Event;
import se.telescopesoftware.betpals.domain.Invitation;

@Repository
public class HibernateCompetitionRepositoryImpl implements CompetitionRepository {

	private SessionFactory sessionFactory;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	public Competition storeCompetition(Competition competition) {
    	Session session = sessionFactory.getCurrentSession();
		return (Competition) session.merge(competition);
	}

	@SuppressWarnings("unchecked")
	public Collection<Competition> loadActiveCompetitionsByUser(Long userId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from Competition c where c.ownerId = :ownerId and c.status != :status order by c.created desc");
    	query.setLong("ownerId", userId);
    	query.setParameter("status", CompetitionStatus.SETTLED);
    	return query.list();
	}
	
	public Integer getActiveCompetitionsByUserCount(Long userId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("select count(*) from Competition c where c.ownerId = :ownerId and c.status != :status");
    	query.setLong("ownerId", userId);
    	query.setParameter("status", CompetitionStatus.SETTLED);
		return DataAccessUtils.intResult(query.list());
	}

	public void storeBet(Bet bet) {
    	Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(bet);
	}

	@SuppressWarnings("unchecked")
	public Collection<Bet> loadActiveBetsByUser(Long userId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from Bet b where b.ownerId = :ownerId");
    	query.setLong("ownerId", userId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public Collection<Bet> loadActiveBetsByUserAndAccount(Long userId, Long accountId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from Bet b where b.ownerId = :ownerId and accountId = :accountId");
    	query.setLong("ownerId", userId);
    	query.setLong("accountId", accountId);
		return query.list();
	}

	public void storeInvitation(Invitation invitation) {
    	Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(invitation);
	}

	public Integer getInvitationsForUserCount(Long userId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("select count(*) from Invitation i where i.inviteeId = :userId");
    	query.setLong("userId", userId);
		return DataAccessUtils.intResult(query.list());
	}

	@SuppressWarnings("unchecked")
	public Collection<Invitation> loadInvitationsForUser(Long userId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from Invitation i where i.inviteeId = :userId");
    	query.setLong("userId", userId);
		return query.list();
	}

	public Invitation loadInvitationById(Long id) {
    	Session session = sessionFactory.getCurrentSession();
		return (Invitation) session.get(Invitation.class, id);
	}

	public Competition loadCompetitionById(Long id) {
    	Session session = sessionFactory.getCurrentSession();
		return (Competition) session.get(Competition.class, id);
	}

	public void deleteInvitation(Invitation invitation) {
    	Session session = sessionFactory.getCurrentSession();
		session.delete(invitation);
	}

	public Event loadEventById(Long id) {
    	Session session = sessionFactory.getCurrentSession();
		return (Event) session.get(Event.class, id);
	}

	public Alternative storeAlternative(Alternative alternative) {
    	Session session = sessionFactory.getCurrentSession();
		return (Alternative) session.merge(alternative);
	}

	public Event storeEvent(Event event) {
    	Session session = sessionFactory.getCurrentSession();
		return (Event) session.merge(event);
	}

	public void deleteCompetition(Competition competition) {
    	Session session = sessionFactory.getCurrentSession();
		session.delete(competition);
	}

	@SuppressWarnings("unchecked")
	public Collection<Bet> loadActiveBetsBySelectionId(Long selectionId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from Bet b where b.selectionId = :selectionId");
    	query.setLong("selectionId", selectionId);
    	return query.list();
	}

	public void deleteBet(Bet bet) {
    	Session session = sessionFactory.getCurrentSession();
		session.delete(bet);
	}

	public Alternative loadAlternativeById(Long id) {
    	Session session = sessionFactory.getCurrentSession();
		return (Alternative) session.get(Alternative.class, id);
	}

	public void deleteInvitationsByCompetitionId(Long competitionId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from Invitation i where i.competitionId = :competitionId");
    	query.setLong("competitionId", competitionId);
		@SuppressWarnings("unchecked")
		Collection<Invitation> invitations = query.list();
		for (Invitation invitation : invitations) {
			deleteInvitation(invitation);
		}
	}

	public Integer getTotalUserCompetitionsCount(Long userId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("select count(*) from Competition c where c.ownerId = :userId");
    	query.setLong("userId", userId);
		return DataAccessUtils.intResult(query.list());
	}

	public Integer getTotalUserBetsCount(Long userId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("select count(*) from Bet b where b.ownerId = :userId");
    	query.setLong("userId", userId);
		return DataAccessUtils.intResult(query.list());
	}

	@SuppressWarnings("unchecked")
	public Collection<Bet> loadSettledBetsByUserAndAccount(Long userId, Long accountId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from Bet b where b.ownerId = :ownerId and accountId = :accountId and settled is not null");
    	query.setLong("ownerId", userId);
    	query.setLong("accountId", accountId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public Collection<Competition> loadCompetitions(Long userId, CompetitionStatus competitionStatus) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from Competition c where c.ownerId = :userId and c.status = :status order by c.created desc");
    	query.setLong("userId", userId);
    	query.setParameter("status", competitionStatus);
		return query.list();
	}

	public Integer getCompetitionsCount(Long userId, CompetitionStatus competitionStatus) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("select count(*) from Competition c where c.ownerId = :ownerId and c.status = :status");
    	query.setLong("ownerId", userId);
    	query.setParameter("status", competitionStatus);
		return DataAccessUtils.intResult(query.list());
	}

}
