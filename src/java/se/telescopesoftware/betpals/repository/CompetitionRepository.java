package se.telescopesoftware.betpals.repository;

import java.util.Collection;

import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.Invitation;

public interface CompetitionRepository {

	Competition storeCompetition(Competition competition);
	
	Collection<Competition> loadActiveCompetitionsByUser(Long userId);
	
	Integer getActiveCompetitionsByUserCount(Long userId);
	
	void storeBet(Bet bet);
	
	Collection<Bet> loadActiveBetsByUser(Long userId);

	Collection<Bet> loadActiveBetsByUserAndAccount(Long userId, Long accountId);
	
	void storeInvitation(Invitation invitation);
	
	Integer getInvitationsForUserCount(Long userId);
	
	Collection<Invitation> loadInvitationsForUser(Long userId);
	
	Invitation loadInvitationById(Long id);
	
	Competition loadCompetitionById(Long id);
	
	void deleteInvitation(Invitation invitation);
}
