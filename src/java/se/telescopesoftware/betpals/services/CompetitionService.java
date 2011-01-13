package se.telescopesoftware.betpals.services;

import java.util.Collection;

import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.Invitation;
import se.telescopesoftware.betpals.domain.UserProfile;

public interface CompetitionService {

	
	Competition addCompetition(Competition competition);
	
	Collection<Competition> getActiveCompetitionsByUser(Long userId);
	
	Integer getActiveCompetitionsByUserCount(Long userId);

	void placeBet(Bet bet);
	
	Collection<Bet> getActiveBetsByUser(Long userId);

	Collection<Bet> getActiveBetsByUserAndAccount(Long userId, Long accountId);
	
	void sendInvitationsToFriends(Competition competition, Collection<Long> friendIds, UserProfile owner);
	
	Integer getInvitationsForUserCount(Long userId);
	
	Collection<Invitation> getInvitationsForUser(Long userId);

	Invitation getInvitationById(Long id);
	
	Competition getCompetitionById(Long id);
	
	void deleteInvitation(Long id);
}
