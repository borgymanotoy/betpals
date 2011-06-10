package se.telescopesoftware.betpals.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.AccountTransaction;
import se.telescopesoftware.betpals.domain.AccountTransactionType;
import se.telescopesoftware.betpals.domain.Alternative;
import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.CompetitionStatus;
import se.telescopesoftware.betpals.domain.CompetitionType;
import se.telescopesoftware.betpals.domain.Event;
import se.telescopesoftware.betpals.domain.Invitation;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.repository.CompetitionRepository;

@Service
@Transactional(readOnly = true)
public class CompetitionServiceImpl implements CompetitionService {

	private CompetitionRepository competitionRepository;
	private AccountService accountService;
	private EmailService emailService;
	private MessageSource messageSource;
	
	//TODO: Move to configuration
	private BigDecimal SYSTEM_COMMISION = new BigDecimal("0.04");
	private int DEFAULT_DEADLINE_INTERVAL = 7;
	private int DEFAULT_SETTLING_INTERVAL = 8;
	
    private static Logger logger = Logger.getLogger(CompetitionServiceImpl.class);

    
    @Autowired
    public void setCompetitionRepository(CompetitionRepository competitionRepository) {
    	this.competitionRepository = competitionRepository;
    }
    
    @Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
    	this.messageSource = messageSource;
    }

    
	@Transactional(readOnly = false)
	public Competition saveCompetition(Competition competition) {
		logger.info("Saving " + competition);
		return competitionRepository.storeCompetition(competition);
	}

	public Collection<Competition> getActiveCompetitionsByUser(Long userId) {
		return competitionRepository.loadActiveCompetitionsByUser(userId);
	}

	public Integer getActiveCompetitionsByUserCount(Long userId) {
		return competitionRepository.getActiveCompetitionsByUserCount(userId);
	}

	@Transactional(readOnly = false)
	public void placeBet(Bet bet) {
		//TODO: Check this!
		Account account = null;
		if (bet.getAccountId() != null) {
			account = accountService.getAccount(bet.getAccountId());
		} else {
			account = accountService.getUserAccountForCurrency(bet.getOwnerId(), bet.getCurrency());
		}

		if (account != null) {
			logger.info("Placing " + bet);
			AccountTransaction transaction = new AccountTransaction(account, bet.getStake().negate(), AccountTransactionType.RESERVATION);
			account.addTransaction(transaction);
			
			//TODO: Add check for available funds
			accountService.saveAccount(account);
			bet.setAccountId(account.getId());
			bet.setCurrency(account.getCurrency());
			Alternative alternative = competitionRepository.loadAlternativeById(bet.getSelectionId());
			alternative.addBet(bet);
			Competition competition = alternative.getEvent().getCompetition();
			if (competition.getCompetitionType() != CompetitionType.POOL_BETTING) {
				alternative.setTaken(true);
			}
			saveAlternative(alternative);
		}
	}

	public Collection<Bet> getActiveBetsByUser(Long userId) {
		return competitionRepository.loadActiveBetsByUser(userId);
	}

	public Collection<Bet> getActiveBetsByUserAndAccount(Long userId, Long accountId) {
		return competitionRepository.loadActiveBetsByUserAndAccount(userId, accountId);
	}

	public Collection<Bet> getSettledBetsByUserAndAccount(Long userId, Long accountId) {
		return competitionRepository.loadSettledBetsByUserAndAccount(userId, accountId);
	}
	
	@Transactional(readOnly = false)
	public void sendInvitationsToFriends(Competition competition, Collection<Long> friendIds, UserProfile owner) {
		for (Long friendId : friendIds) {
			Invitation invitation = new Invitation(competition, owner, friendId);
			logger.info("Sending " + invitation);
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

	@Transactional(readOnly = false)
	public void deleteInvitation(Long id) {
		Invitation invitation = competitionRepository.loadInvitationById(id);
		//TODO: Add check for ownership
		logger.info("Deleting " + invitation);
		competitionRepository.deleteInvitation(invitation);
	}

	public Event getEventById(Long id) {
		return competitionRepository.loadEventById(id);
	}

	@Transactional(readOnly = false)
	public Alternative saveAlternative(Alternative alternative) {
		logger.info("Saving " + alternative);
		return competitionRepository.storeAlternative(alternative);
	}

	@Transactional(readOnly = false)
	public Event saveEvent(Event event) {
		logger.info("Saving " + event);
		return competitionRepository.storeEvent(event);
	}

	@Transactional(readOnly = false, noRollbackFor = {MessagingException.class, AddressException.class})
	public void deleteCompetition(Long id) {
		Competition competition = getCompetitionById(id);
		logger.info("Deleting " + competition);
		for (Alternative alternative : competition.getAllAlternatives()) {
			voidAlternative(alternative, competition, null);
		}
		competitionRepository.deleteCompetition(competition);
		competitionRepository.deleteInvitationsByCompetitionId(competition.getId());
		check();
	}
	
	//TODO: remove this
	private void check() {
		List list = new ArrayList();
		Object o = list.get(5);
	}

	/**
	 *  The formula for settling pool bets is "part * (turnover - commision)"
	 *  
	 *  Example:
	 *  
	 *	Alt A and B
	 *	Peter placed 100 on A
	 *	Pavel placed 300 on A
	 *	I placed 200 on B
	 *	A was the right alternative
	 *	Peter get 0,25 * 600 = 150
	 *	Pavel gets 0,75 * 600 = 450
	 *	And I get mad
	 */
	@Transactional(readOnly = false)
	public void settleCompetition(Long competitionId, Long alternativeId) {
		Competition competition = competitionRepository.loadCompetitionById(competitionId);
		competition.setStatus(CompetitionStatus.SETTLED);
		logger.info("Settling " + competition);
		for (Alternative alternative : competition.getAllAlternatives()) {
			if (alternative.getId().compareTo(alternativeId) == 0) {
				alternativeWon(alternative, competition);
			} else {
				alternativeLost(alternative);
			}
		}
		saveCompetition(competition);
		competitionRepository.deleteInvitationsByCompetitionId(competitionId);
	}

	private void alternativeWon(Alternative alternative, Competition competition) {
		logger.info("Won " + alternative);
		for (Bet bet : alternative.getBets()) {
			logger.info("Settling " + bet);
			BigDecimal part = bet.getStake().divide(alternative.getTurnover(), 2, RoundingMode.HALF_UP); //TODO: Find out about required precision
			BigDecimal competitionTurnoverWithCommission = calculateAmountWithCommission(competition.getTurnover()) ;
			BigDecimal amountWon = part.multiply(competitionTurnoverWithCommission).subtract(bet.getStake());
			Account account = accountService.getAccount(bet.getAccountId());
			AccountTransaction transaction = new AccountTransaction(account, bet.getStake(), AccountTransactionType.RESERVATION);
			logger.info(transaction);
			account.addTransaction(transaction);
			transaction = new AccountTransaction(account, amountWon, AccountTransactionType.WON);
			logger.info(transaction);
			account.addTransaction(transaction);
			
			accountService.saveAccount(account);
			bet.setSettled(new Date());
			bet.setProfitOrLoss(amountWon);
			saveAlternative(alternative);
		}
	}

	private void alternativeLost(Alternative alternative) {
		logger.info("Lost " + alternative);
		for (Bet bet : alternative.getBets()) {
			logger.info("Settling " + bet);
			Account account = accountService.getAccount(bet.getAccountId());
			AccountTransaction transaction = new AccountTransaction(account, bet.getStake().negate(), AccountTransactionType.LOST);
			logger.info(transaction);
			account.addTransaction(transaction);
			
			accountService.saveAccount(account);
			bet.setSettled(new Date());
			bet.setProfitOrLoss(bet.getStake().negate());
			saveAlternative(alternative);
		}
	}
	
	private BigDecimal calculateAmountWithCommission(BigDecimal amount) {
		BigDecimal percentage = amount.multiply(SYSTEM_COMMISION);
		return amount.subtract(percentage);
	}
	
	@Transactional(readOnly = false, noRollbackFor = {MessagingException.class, AddressException.class})
	public void voidAlternative(Long competitionId, Long alternativeId, Locale locale) {
		Competition competition = getCompetitionById(competitionId);
		voidAlternative(competition.getAlternativeById(alternativeId), competition, locale);
	}

	private void voidAlternative(Alternative alternative, Competition competition, Locale locale) {
		logger.info("Voiding " + alternative);
		notifyPunters(alternative, competition, locale);
	
		for(Bet bet : alternative.getBets()) {
			logger.info("Removing " + bet);
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
		saveCompetition(competition);
	}
	
	private void notifyPunters(Alternative alternative, Competition competition, Locale locale) {
		for (Long participantId : alternative.getParticipantsIdSet()) {
			String subject = messageSource.getMessage("email.void.alternative.subject", new Object[] {alternative.getName()}, locale);
			String text = messageSource.getMessage("email.void.alternative.text", new Object[] {alternative.getName(), competition.getName()}, locale);
			try {
				logger.info("Notify punter: " + participantId);
				emailService.sendEmail(competition.getOwnerId(), participantId, subject, text);
			} catch (AddressException ex) {
				logger.error("Incorrect email address", ex);
			} catch (MessagingException ex) {
				logger.error("Could not send email", ex);
			}
		}
	}
	
	public Collection<Bet> getActiveBetsBySelectionId(Long selectionId) {
		return competitionRepository.loadActiveBetsBySelectionId(selectionId);
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
			Alternative alternative = competitionRepository.loadAlternativeById(bet.getSelectionId());
			alternative.removeBet(bet);
			saveAlternative(alternative);
		}
	}

	public Collection<Competition> getOngoingCompetitionsByUser(Long userId) {
		Set<Competition> result = new HashSet<Competition>();
		for (Bet bet : getActiveBetsByUser(userId)) {
			try {
				Competition competition = bet.getAlternative().getEvent().getCompetition();
				if (competition.getStatus() != CompetitionStatus.SETTLED) {
					result.add(competition);
				}
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		
		return result;
	}

	public Integer getOngoingCompetitionsByUserCount(Long userId) {
		//TODO: Rewrite to something more effective
		return getOngoingCompetitionsByUser(userId).size();
	}

	public Integer getTotalUserCompetitionsCount(Long userId) {
		return competitionRepository.getTotalUserCompetitionsCount(userId);
	}

	public Integer getTotalUserBetsCount(Long userId) {
		return competitionRepository.getTotalUserBetsCount(userId);
	}

	public Collection<Competition> getSettledCompetitionsByUser(Long userId) {
		return competitionRepository.loadCompetitions(userId, CompetitionStatus.SETTLED);
	}

	public Integer getSettledCompetitionsByUserCount(Long userId) {
		return competitionRepository.getCompetitionsCount(userId, CompetitionStatus.SETTLED);
	}

	public int getDefaultDeadlineInterval() {
		return DEFAULT_DEADLINE_INTERVAL;
	}

	public int getDefaultSettlingInterval() {
		return DEFAULT_SETTLING_INTERVAL;
	}

	public Collection<Competition> getNewCompetitionsByUser(Long userId) {
		return competitionRepository.loadCompetitions(userId, CompetitionStatus.NEW);
	}

	public Integer getNewCompetitionsByUserCount(Long userId) {
		return competitionRepository.getCompetitionsCount(userId, CompetitionStatus.NEW);
	}


}
