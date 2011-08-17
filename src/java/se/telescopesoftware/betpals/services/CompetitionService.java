package se.telescopesoftware.betpals.services;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import se.telescopesoftware.betpals.domain.Alternative;
import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.CompetitionLogEntry;
import se.telescopesoftware.betpals.domain.Event;
import se.telescopesoftware.betpals.domain.Invitation;
import se.telescopesoftware.betpals.domain.UserProfile;

public interface CompetitionService {

	
	Competition saveCompetition(Competition competition, Locale locale, boolean sendEmail);
	
	Collection<Competition> getActiveCompetitionsByUser(Long userId);

	Collection<Competition> getSettledCompetitionsByUser(Long userId);

	Collection<Competition> getOngoingCompetitionsByUser(Long userId);

	Collection<Competition> getNewCompetitionsByUser(Long userId);

	Collection<Competition> getAllCompetitions(Integer pageNumber, Integer itemsPerPage);
	
	Integer getActiveCompetitionsByUserCount(Long userId);

	Integer getSettledCompetitionsByUserCount(Long userId);

	Integer getOngoingCompetitionsByUserCount(Long userId);

	Integer getNewCompetitionsByUserCount(Long userId);

	void placeBet(Bet bet);
	
	Collection<Bet> getActiveBetsByUser(Long userId);

	Collection<Bet> getActiveBetsByUserAndAccount(Long userId, Long accountId);

	Collection<Bet> getSettledBetsByUserAndAccount(Long userId, Long accountId);
	
	Collection<Bet> getActiveBetsBySelectionId(Long selectionId);
	
	void sendInvitationsToFriends(Competition competition, Collection<Long> friendIds, UserProfile owner, Locale locale);
	
	Integer getInvitationsForUserCount(Long userId);
	
	Collection<Invitation> getInvitationsForUser(Long userId);

	Collection<Invitation> getInvitationsForCommunity(Long communityId);
	
	Invitation getInvitationById(Long id);
	
	Competition getCompetitionById(Long id);
	
	Event getEventById(Long id);
	
	Alternative saveAlternative(Alternative alternative);
	
	void deleteInvitation(Long id);
	
	Event saveEvent(Event event);
	
	void deleteCompetition(Long id);
	
	void settleCompetition(Long competitionId, Long alternativeId, Locale locale);
	
	void voidAlternative(Long competitionId, Long alternativeId, Locale locale);
	
	Integer getTotalUserCompetitionsCount(Long userId);

	Integer getTotalUserBetsCount(Long userId);

	Date getLastCompetitionCreatedDate(Long userId);
	
	Date getLastBetPlacedDate(Long userId);
	
	int getDefaultDeadlineInterval();

	int getDefaultSettlingInterval();
	
    void saveCompetitionLogEntry(CompetitionLogEntry competitionLogEntry);
    
    Collection<CompetitionLogEntry> getCompetitionLogEntries(Long competitionId);
	
    void processExpiredCompetitions();

	void sendInvitationsToCommunities(Competition competition,
			Set<Long> communityIdSet, UserProfile userProfile, Locale locale);

}
