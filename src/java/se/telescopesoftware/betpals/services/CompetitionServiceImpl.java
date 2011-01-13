package se.telescopesoftware.betpals.services;

import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.AccountTransaction;
import se.telescopesoftware.betpals.domain.AccountTransactionType;
import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityType;
import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Competition;
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
		return competitionRepository.loadActiveCompetitionsByUser(userId);
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
			AccountTransaction transaction = new AccountTransaction(account, bet.getStake(), AccountTransactionType.RESERVATION);
			account.addTransaction(transaction);
			
			//TODO: Add check for available funds
			accountService.saveAccount(account);
			bet.setAccountId(account.getId());
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
    	Activity activity = new Activity();
    	activity.setCreated(new Date());
    	activity.setOwnerId(owner.getUserId());
    	activity.setOwnerName(owner.getFullName());
    	activity.setActivityType(ActivityType.MESSAGE);
    	activity.setMessage("Created new competition: " + competition.getName());
    	
    	activityService.addActivity(activity);

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
		return competitionRepository.loadCompetitionById(id);
	}

	public void deleteInvitation(Long id) {
		Invitation invitation = competitionRepository.loadInvitationById(id);
		//TODO: Add check for ownership
		competitionRepository.deleteInvitation(invitation);
	}


}
