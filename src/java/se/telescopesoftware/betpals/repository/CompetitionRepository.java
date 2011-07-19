package se.telescopesoftware.betpals.repository;

import java.util.Collection;
import java.util.Date;

import se.telescopesoftware.betpals.domain.Alternative;
import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.CompetitionLogEntry;
import se.telescopesoftware.betpals.domain.CompetitionStatus;
import se.telescopesoftware.betpals.domain.Event;
import se.telescopesoftware.betpals.domain.Invitation;

public interface CompetitionRepository {

	Competition storeCompetition(Competition competition);
	
	Collection<Competition> loadActiveCompetitionsByUser(Long userId);

	Collection<Competition> loadCompetitions(Long userId, CompetitionStatus competitionStatus);

	Collection<Competition> loadCompetitions(Integer pageNumber, Integer itemsPerPage);
	
	Integer getActiveCompetitionsByUserCount(Long userId);

	Integer getCompetitionsCount(Long userId, CompetitionStatus competitionStatus);
	
	void storeBet(Bet bet);
	
	Collection<Bet> loadActiveBetsByUser(Long userId);

	Collection<Bet> loadActiveBetsByUserAndAccount(Long userId, Long accountId);

	Collection<Bet> loadSettledBetsByUserAndAccount(Long userId, Long accountId);
	
	Collection<Bet> loadActiveBetsBySelectionId(Long selectionId);
	
	void storeInvitation(Invitation invitation);
	
	Integer getInvitationsForUserCount(Long userId);
	
	Collection<Invitation> loadInvitationsForUser(Long userId);
	
	Invitation loadInvitationById(Long id);
	
	Competition loadCompetitionById(Long id);
	
	void deleteInvitation(Invitation invitation);
	
	void deleteInvitationsByCompetitionId(Long competitionId);
	
	Event loadEventById(Long id);
	
	Alternative storeAlternative(Alternative alternative);
	
	Alternative loadAlternativeById(Long id);
	
	Event storeEvent(Event event);
	
	void deleteCompetition(Competition competition);
	
	void deleteBet(Bet bet);
	
	Integer getTotalUserCompetitionsCount(Long userId);

	Integer getTotalUserBetsCount(Long userId);
	
	Date getLastCompetitionCreatedDate(Long userId);
	
	Date getLastBetPlacedDate(Long userId);

    void storeCompetitionLogEntry(CompetitionLogEntry competitionLogEntry);
    
    Collection<CompetitionLogEntry> loadCompetitionLogEntries(Long competitionId);
	
}
