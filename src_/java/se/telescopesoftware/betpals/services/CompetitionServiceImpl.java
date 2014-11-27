package se.telescopesoftware.betpals.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import se.telescopesoftware.betpals.domain.AccessType;
import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.AccountTransaction;
import se.telescopesoftware.betpals.domain.AccountTransactionType;
import se.telescopesoftware.betpals.domain.Alternative;
import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.CompetitionLogEntry;
import se.telescopesoftware.betpals.domain.CompetitionStatus;
import se.telescopesoftware.betpals.domain.CompetitionTurnoverComparator;
import se.telescopesoftware.betpals.domain.CompetitionType;
import se.telescopesoftware.betpals.domain.Event;
import se.telescopesoftware.betpals.domain.Invitation;
import se.telescopesoftware.betpals.domain.InvitationType;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.repository.CompetitionRepository;

@Service
@Transactional(readOnly = true)
public class CompetitionServiceImpl implements CompetitionService {

	private CompetitionRepository competitionRepository;
	private AccountService accountService;
	private UserService userService;
	private EmailService emailService;
	private MessageSource messageSource;
	private SiteConfigurationService siteConfigurationService;
	private VelocityEngine velocityEngine;
	
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
    public void setUserService(UserService userService) {
    	this.userService = userService;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
    	this.messageSource = messageSource;
    }

    @Autowired
    public void setSiteConfigurationService(SiteConfigurationService siteConfigurationService) {
    	this.siteConfigurationService = siteConfigurationService;
    }
    
    @Autowired
    public void setVelocityEngine(VelocityEngine velocityEngine) {
    	this.velocityEngine = velocityEngine;
    }
    
    
	@Transactional(readOnly = false)
	public Competition saveCompetition(Competition competition, Locale locale, boolean sendEmail) {
		logger.info("Saving " + competition);
		Competition storedCompetition = competitionRepository.storeCompetition(competition);
		saveCompetitionLogEntry(storedCompetition.getId(), "Competition saved");
		if (sendEmail) {
			sendModifiedEmail(storedCompetition, locale);
		}
		return storedCompetition;
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

		if (account != null && account.isValidStake(bet.getStake())) {
			logger.info("Placing " + bet);
			AccountTransaction transaction = new AccountTransaction(account, bet.getStake().negate(), AccountTransactionType.RESERVATION);
			account.addTransaction(transaction);
			
			accountService.saveAccount(account);
			bet.setAccountId(account.getId());
			bet.setCurrency(account.getCurrency());
			Alternative alternative = competitionRepository.loadAlternativeById(bet.getSelectionId());
			alternative.addBet(bet);
			Competition competition = alternative.getEvent().getCompetition();
			saveCompetitionLogEntry(competition.getId(), "Bet placed: " + bet);
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
	public void sendInvitationsToFriends(Competition competition, Collection<Long> friendIds, UserProfile owner, Locale locale) {
		for (Long friendId : friendIds) {
			Invitation invitation = new Invitation(competition, owner, friendId);
			logger.info("Sending " + invitation);
			saveCompetitionLogEntry(competition.getId(), "Sending " + invitation);
			competitionRepository.storeInvitation(invitation);
			UserProfile friendProfile = userService.getUserProfileByUserId(friendId);
			if (friendProfile.isEmailOnBetInvitation()) {
				sendInvitationByMail(invitation, locale);
			}
		}
	}

	@Transactional(readOnly = false)
	public void sendInvitationsToCommunities(Competition competition, Set<Long> communitiesIds, UserProfile owner, Locale locale) {
		for (Long communityId : communitiesIds) {
			Invitation invitation = new Invitation(competition, owner, communityId, InvitationType.COMMUNITY);
			logger.info("Sending " + invitation);
			saveCompetitionLogEntry(competition.getId(), "Sending " + invitation);
			competitionRepository.storeInvitation(invitation);
		}
	}
	
	public Integer getInvitationsForUserCount(Long userId) {
		return competitionRepository.getInvitationsForUserCount(userId);
	}

	public Collection<Invitation> getInvitationsForUser(Long userId) {
		return competitionRepository.loadInvitationsForUser(userId);
	}

	public Collection<Invitation> getInvitationsForCommunity(Long communityId) {
		return competitionRepository.loadInvitationsForCommunity(communityId);
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
		logger.info("Deleting " + invitation);
		competitionRepository.deleteInvitation(invitation);
	}

	public Event getEventById(Long id) {
		return competitionRepository.loadEventById(id);
	}

	@Transactional(readOnly = false)
	public Alternative saveAlternative(Alternative alternative) {
		logger.info("Saving " + alternative);
		saveCompetitionLogEntry(alternative.getCompetitionId(), "Saving " + alternative);
		return competitionRepository.storeAlternative(alternative);
	}

	@Transactional(readOnly = false)
	public Event saveEvent(Event event) {
		logger.info("Saving " + event);
		saveCompetitionLogEntry(event.getCompetitionId(), "Saving " + event);
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
	public void settleCompetition(Long competitionId, Long alternativeId, Locale locale) {
		Competition competition = competitionRepository.loadCompetitionById(competitionId);
		competition.setStatus(CompetitionStatus.SETTLED);
		logger.info("Settling " + competition);
		saveCompetitionLogEntry(competitionId, "Settling competition");

		for (Alternative alternative : competition.getAllAlternatives()) {
			if (alternative.getId().compareTo(alternativeId) == 0) {
				alternativeWon(alternative, competition);
				sendSettleEmail(competition, alternative, locale);
			} else {
				alternativeLost(alternative);
			}
		}
		saveCompetition(competition, null, false);
		competitionRepository.deleteInvitationsByCompetitionId(competitionId);
	}

	private void alternativeWon(Alternative alternative, Competition competition) {
		logger.info("Won " + alternative);
		saveCompetitionLogEntry(competition.getId(), "Won " + alternative);

		for (Bet bet : alternative.getBets()) {
			logger.info("Settling " + bet);
			saveCompetitionLogEntry(competition.getId(), "Settling " + bet);

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
		saveCompetitionLogEntry(alternative.getCompetitionId(), "Lost " + alternative);

		for (Bet bet : alternative.getBets()) {
			logger.info("Settling " + bet);
			saveCompetitionLogEntry(alternative.getCompetitionId(), "Settling " + bet);
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
		BigDecimal systemCommision = new BigDecimal(siteConfigurationService.getParameterValue("system.commision", "0.04"));
		BigDecimal percentage = amount.multiply(systemCommision);
		return amount.subtract(percentage);
	}
	
	@Transactional(readOnly = false, noRollbackFor = {MessagingException.class, AddressException.class})
	public void voidAlternative(Long competitionId, Long alternativeId, Locale locale) {
		Competition competition = getCompetitionById(competitionId);
		voidAlternative(competition.getAlternativeById(alternativeId), competition, locale);
	}

	private void voidAlternative(Alternative alternative, Competition competition, Locale locale) {
		logger.info("Voiding " + alternative);
		saveCompetitionLogEntry(alternative.getCompetitionId(), "Voiding " + alternative);

		notifyPunters(alternative, competition, locale);
	
		for(Bet bet : alternative.getBets()) {
			logger.info("Removing " + bet);
			saveCompetitionLogEntry(alternative.getCompetitionId(), "Removing " + bet);
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
		saveCompetition(competition, locale, true);
	}
	
	private void notifyPunters(Alternative alternative, Competition competition, Locale locale) {
		for (Long participantId : alternative.getParticipantsIdSet()) {
			String subject = messageSource.getMessage("email.void.alternative.subject", new Object[] {alternative.getName()}, locale);
			String text = messageSource.getMessage("email.void.alternative.text", new Object[] {alternative.getName(), competition.getName()}, locale);
			try {
				logger.info("Notify punter: " + participantId);
				saveCompetitionLogEntry(competition.getId(), "Notify punter: " + participantId);
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
				Alternative alternative = bet.getAlternative();
				Event event = alternative.getEvent();
				if (event != null) {
					Competition competition = event.getCompetition();
					if (competition.getStatus() != CompetitionStatus.SETTLED) {
						result.add(competition);
					}
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
		return new Integer(siteConfigurationService.getParameterValue("interval.deadline.days", "7")).intValue();
	}

	public int getDefaultSettlingInterval() {
		return new Integer(siteConfigurationService.getParameterValue("interval.settling.days", "8")).intValue();
	}

	public Collection<Competition> getNewCompetitionsByUser(Long userId) {
		return competitionRepository.loadCompetitions(userId, CompetitionStatus.NEW);
	}

	public Integer getNewCompetitionsByUserCount(Long userId) {
		return competitionRepository.getCompetitionsCount(userId, CompetitionStatus.NEW);
	}

	public Date getLastCompetitionCreatedDate(Long userId) {
		return competitionRepository.getLastCompetitionCreatedDate(userId);
	}

	public Date getLastBetPlacedDate(Long userId) {
		return competitionRepository.getLastBetPlacedDate(userId);
	}

	public Collection<Competition> getAllCompetitions(Integer pageNumber, Integer itemsPerPage) {
		return competitionRepository.loadCompetitions(pageNumber != null ? pageNumber : new Integer(0),	itemsPerPage);
	}

	public Collection<Competition> getAllActiveCompetitionsByAccessType(AccessType accessType) {
		return competitionRepository.loadAllActiveCompetitionsByAccessType(null, null, accessType);
	}
	
	public Collection<Competition> getActiveCompetitionsByAccessType(Integer pageNumber, Integer itemsPerPage, AccessType accessType) {
		int competitionsPerPage = new Integer(siteConfigurationService.getParameterValue("competitions.per.page", "10")).intValue();
		return competitionRepository.loadAllActiveCompetitionsByAccessType(pageNumber != null ? pageNumber : new Integer(0), 
				itemsPerPage != null ? itemsPerPage : competitionsPerPage, accessType);
	}
	
	@Transactional(readOnly = false)
	public void saveCompetitionLogEntry(CompetitionLogEntry competitionLogEntry) {
		competitionRepository.storeCompetitionLogEntry(competitionLogEntry);
	}

	public Collection<CompetitionLogEntry> getCompetitionLogEntries(Long competitionId) {
		return competitionRepository.loadCompetitionLogEntries(competitionId);
	}

	private void saveCompetitionLogEntry(Long competitionId, String message) {
		CompetitionLogEntry logEntry = new CompetitionLogEntry(competitionId, message);
		saveCompetitionLogEntry(logEntry);
	}
	
	private void sendInvitationByMail(Invitation invitation, Locale locale) {
		try {
			String language = locale != null ? locale.getLanguage() : "en";
			String template = "competitionInvitation_" + language + ".vm";
			Map<String, Object> model = new HashMap<String, Object>();
            model.put("invitation", invitation);

            String message = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, model);
			String subject = messageSource.getMessage("email.competition.invitation.subject", new Object[] {invitation.getCompetitionName()}, locale);
			emailService.sendEmail(invitation.getOwnerId(), invitation.getInviteeId(), subject, message);
		} catch (Exception e) {
			logger.error("Could not send invitation email: ", e);
		}

	}
	
	private void sendSettleEmail(Competition competition, Alternative winAlternative, Locale locale) {
		try {
			String language = locale != null ? locale.getLanguage() : "en";
			String template = "competitionSettled_" + language + ".vm";
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("competition", competition);
			model.put("alternative", winAlternative);
			
			String message = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, model);
			String subject = messageSource.getMessage("email.competition.settled.subject", new Object[] {competition.getName()}, locale);
			
			for (Long participantId : competition.getParticipantsIdSet()) {
				emailService.sendEmail(competition.getOwnerId(), participantId, subject, message);
			}
			
		} catch (Exception e) {
			logger.error("Could not send email: ", e);
		}
		
	}
	
	private void sendModifiedEmail(Competition competition, Locale locale) {
		try {
			String language = locale != null ? locale.getLanguage() : "en";
			String template = "competitionModified_" + language + ".vm";
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("competition", competition);
			
			String message = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, model);
			String subject = messageSource.getMessage("email.competition.modified.subject", new Object[] {competition.getName()}, locale);
			
			for (Long participantId : competition.getParticipantsIdSet()) {
				emailService.sendEmail(competition.getOwnerId(), participantId, subject, message);
			}
			
		} catch (Exception e) {
			logger.error("Could not send email: ", e);
		}
		
	}

	private void sendVoidEmail(Competition competition, Locale locale) {
		try {
			String language = locale != null ? locale.getLanguage() : "en";
			String template = "competitionVoided_" + language + ".vm";
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("competition", competition);
			
			String message = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, model);
			String subject = messageSource.getMessage("email.competition.voided.subject", new Object[] {competition.getName()}, locale);
			
			for (Long participantId : competition.getParticipantsIdSet()) {
				emailService.sendEmail(competition.getOwnerId(), participantId, subject, message);
			}
			
		} catch (Exception e) {
			logger.error("Could not send email: ", e);
		}
		
	}
	
	private void sendSettlingNotificationEmail(Competition competition, Locale locale) {
		try {
			String language = locale != null ? locale.getLanguage() : "en";
			String template = "competitionSettleNotification_" + language + ".vm";
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("competition", competition);
			
			String message = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, model);
			String subject = messageSource.getMessage("email.competition.settle.notification.subject", new Object[] {competition.getName()}, locale);
			
			emailService.sendEmail(competition.getOwnerId(), competition.getOwnerId(), subject, message);
			
		} catch (Exception e) {
			logger.error("Could not send email: ", e);
		}
		
	}
	
	@Transactional(readOnly = false, noRollbackFor = {SendFailedException.class, MessagingException.class, AddressException.class})
	public void processExpiredCompetitions() {
		logger.info("Processing expired competitions");
		Collection<Competition> competitions = competitionRepository.loadAllActiveCompetitions(null, null);
		int expirationDaysInterval = new Integer(siteConfigurationService.getParameterValue("interval.competition.expiration.days", "7")).intValue();
		int notificationDaysInterval = new Integer(siteConfigurationService.getParameterValue("interval.competition.notification.days", "3")).intValue();
		for (Competition competition : competitions) {
			if (competition.isExpired(expirationDaysInterval)) {
				sendVoidEmail(competition, null); //TODO: Store locale in Competition?
				deleteCompetition(competition.getId()); 
			} else if (competition.isExpirationNotificationNeeded(notificationDaysInterval)) {
				sendSettlingNotificationEmail(competition, null); //TODO: Store locale in Competition?
			}
		}
	}

	public Collection<Competition> getTopPublicCompetitionsByTurnover(Integer numberOfCompetitions) {
		List<Competition> competitions = new ArrayList<Competition>(getAllActiveCompetitionsByAccessType(AccessType.PUBLIC));
		Collections.sort(competitions, new CompetitionTurnoverComparator());
		if (competitions.size() < numberOfCompetitions) {
			return competitions;
		}
		return competitions.subList(0, numberOfCompetitions);
	}

	public Collection<Competition> searchPublicCompetitions(String query) {
		return competitionRepository.findPublicCompetitions(query);
	}

	public Integer getCompetitionPageCountForAccessType(AccessType accessType, Integer itemsPerPage) {
		Integer totalCount = competitionRepository.getActiveCompetitionsCountByAccessType(accessType);
		if (itemsPerPage == null) {
			itemsPerPage = new Integer(siteConfigurationService.getParameterValue("competitions.per.page", "10")).intValue();
		}
		
        if (totalCount != null) {
        	Integer result = new Integer(totalCount.intValue() / itemsPerPage.intValue());
        	if (result.intValue() != 0 && (result.intValue() * itemsPerPage.intValue()) < totalCount.intValue()) {
        		result += 1;
        	}
            return result;
        }

        return new Integer(0);
	}

	@Override
	public String getLinkToCompetition(Competition competition) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a href=\"");
		sb.append(siteConfigurationService.getParameterValue("site.url", "http://www.mybetpals.com"));
		sb.append("/ongoingcompetition.html?competitionId=");
		sb.append(competition.getId());
		sb.append("\">");
		sb.append(competition.getName());
		sb.append("</a>");
		return sb.toString();
	}

}
