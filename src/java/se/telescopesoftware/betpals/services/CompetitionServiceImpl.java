package se.telescopesoftware.betpals.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.AccountTransaction;
import se.telescopesoftware.betpals.domain.AccountTransactionType;
import se.telescopesoftware.betpals.domain.Alternative;
import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.Event;
import se.telescopesoftware.betpals.domain.Invitation;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.repository.CompetitionRepository;

public class CompetitionServiceImpl implements CompetitionService {

	private CompetitionRepository competitionRepository;
	private AccountService accountService;
	private ActivityService activityService;
	
    private static Logger logger = Logger.getLogger(CompetitionServiceImpl.class);

    
    public void setCompetitionRepository(CompetitionRepository competitionRepository) {
    	this.competitionRepository = competitionRepository;
    }
    
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    
	public Competition addCompetition(Competition competition) {
		return competitionRepository.storeCompetition(competition);
	}

	public Collection<Competition> getActiveCompetitionsByUser(Long userId) {
		return populateBets(competitionRepository.loadActiveCompetitionsByUser(userId));
	}

	public Integer getActiveCompetitionsByUserCount(Long userId) {
		return competitionRepository.getActiveCompetitionsByUserCount(userId);
	}

	public void placeBet(Bet bet) {
		//TODO: Check this!
		Account account = null;
		if (bet.getAccountId() != null) {
			account = accountService.getAccount(bet.getAccountId());
		} else {
			account = accountService.getUserAccountForCurrency(bet.getOwnerId(), bet.getCurrency());
		}

		if (account != null) {
			AccountTransaction transaction = new AccountTransaction(account, bet.getStake().negate(), AccountTransactionType.RESERVATION);
			account.addTransaction(transaction);
			
			//TODO: Add check for available funds
			accountService.saveAccount(account);
			bet.setAccountId(account.getId());
			bet.setCurrency(account.getCurrency());
			competitionRepository.storeBet(bet);
		}
	}

	public Collection<Bet> getActiveBetsByUser(Long userId) {
		return competitionRepository.loadActiveBetsByUser(userId);
	}

	public Collection<Bet> getActiveBetsByUserAndAccount(Long userId, Long accountId) {
		return competitionRepository.loadActiveBetsByUserAndAccount(userId, accountId);
	}

	public void sendInvitationsToFriends(Competition competition, Collection<Long> friendIds, UserProfile owner) {
		for (Long friendId : friendIds) {
			Invitation invitation = new Invitation(competition, owner, friendId);
			logger.debug(invitation.toString());
			competitionRepository.storeInvitation(invitation);
		}
	}

	public Integer getInvitationsForUserCount(Long userId) {
		return competitionRepository.getInvitationsForUserCount(userId);
	}

	public Collection<Invitation> getInvitationsForUser(Long userId) {
		return competitionRepository.loadInvitationsForUser(userId);
	}

	public Invitation getInvitationById(Long id) {
		return competitionRepository.loadInvitationById(id);
	}

	public Competition getCompetitionById(Long id) {
		Competition competition = competitionRepository.loadCompetitionById(id);
		for (Alternative alternative : competition.getAllAlternatives()) {
			alternative.getBets().addAll(getActiveBetsBySelectionId(alternative.getId()));
		}

		return competition;
	}

	public void deleteInvitation(Long id) {
		Invitation invitation = competitionRepository.loadInvitationById(id);
		//TODO: Add check for ownership
		competitionRepository.deleteInvitation(invitation);
	}

	public Event getEventById(Long id) {
		return competitionRepository.loadEventById(id);
	}

	public Alternative saveAlternative(Alternative alternative) {
		return competitionRepository.storeAlternative(alternative);
	}

	public Event saveEvent(Event event) {
		return competitionRepository.storeEvent(event);
	}

	public void deleteCompetition(Long id) {
		Competition competition = getCompetitionById(id);
		logger.info("Delete " + competition.toString());
		for (Alternative alternative : competition.getAllAlternatives()) {
			voidAlternative(alternative, competition);
		}
		competitionRepository.deleteCompetition(competition);
	}

	public void settleCompetition(Long id) {
		// TODO Auto-generated method stub
		Competition competition = competitionRepository.loadCompetitionById(id);
		logger.info("Settle " + competition.toString());
	}

	public void voidAlternative(Long competitionId, Long alternativeId) {
		Competition competition = getCompetitionById(competitionId);
		voidAlternative(competition.getAlternativeById(alternativeId), competition);
	}

	private void voidAlternative(Alternative alternative, Competition competition) {
		logger.info("Void " + alternative.toString());
		for(Bet bet : alternative.getBets()) {
			removeBet(bet);
		}

		//TODO: Add multi event logic
		Set<Alternative> filteredAlternatives = new HashSet<Alternative>();
		for (Alternative storedAlternative : competition.getDefaultEvent().getAlternatives() ) {
			if (storedAlternative.getId().compareTo(alternative.getId()) != 0) {
				filteredAlternatives.add(storedAlternative);
			}
		}
		competition.getDefaultEvent().setAlternatives(filteredAlternatives);
		addCompetition(competition);

	}
	
	public Collection<Bet> getActiveBetsBySelectionId(Long selectionId) {
		return competitionRepository.loadActiveBetsBySelectionId(selectionId);
	}

	private Collection<Competition> populateBets(Collection<Competition> competitionList) {
		for (Competition competition : competitionList) {
			for (Alternative alternative : competition.getAllAlternatives()) {
				alternative.getBets().addAll(getActiveBetsBySelectionId(alternative.getId()));
			}
		}
		return competitionList;
	}
	
	private void removeBet(Bet bet) {
		//TODO: Check this!
		Account account = null;
		if (bet.getAccountId() != null) {
			account = accountService.getAccount(bet.getAccountId());
		} else {
			account = accountService.getUserAccountForCurrency(bet.getOwnerId(), bet.getCurrency());
		}

		if (account != null) {
			AccountTransaction transaction = new AccountTransaction(account, bet.getStake(), AccountTransactionType.RESERVATION);
			account.addTransaction(transaction);
			
			accountService.saveAccount(account);
			competitionRepository.deleteBet(bet);
		}
	}


}
