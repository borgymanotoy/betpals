package se.telescopesoftware.betpals.web;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityType;
import se.telescopesoftware.betpals.domain.Alternative;
import se.telescopesoftware.betpals.domain.AlternativeType;
import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Community;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.CompetitionStatus;
import se.telescopesoftware.betpals.domain.CompetitionType;
import se.telescopesoftware.betpals.domain.Event;
import se.telescopesoftware.betpals.domain.Group;
import se.telescopesoftware.betpals.domain.Invitation;
import se.telescopesoftware.betpals.domain.InvitationHelper;
import se.telescopesoftware.betpals.domain.QuickCompetition;
import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.ActivityService;
import se.telescopesoftware.betpals.services.CompetitionService;
import se.telescopesoftware.betpals.services.FacebookService;

@Controller
public class CompetitionController extends AbstractPalsController {

	private CompetitionService competitionService;
	private AccountService accountService;
	private ActivityService activityService;
	private FacebookService facebookService;
	
    
    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Autowired
	public void setFacebookService(FacebookService facebookService) {
		this.facebookService = facebookService;
	}
	

	@RequestMapping(value="/competitionview")	
	public String getView(@RequestParam(value="competitionId", required=false) Long competitionId, Model model) {
		Competition competition = null;
		if (competitionId != null) {
			competition = competitionService.getCompetitionById(competitionId);
		}
		model.addAttribute(competition != null ? competition : new Competition());
		return "createCompetitionView";
	}
	
	@RequestMapping(value="/competitionconfirmview")	
	public String getConfirmCompetitionView(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		model.addAttribute(competition);
		return "confirmCompetitionView";
	}
	
	@RequestMapping(value="/confirmcompetition")	
	public String confirmCompetition(@RequestParam("competitionId") Long competitionId, HttpSession session, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		competition.setStatus(CompetitionStatus.OPEN);
		competition = competitionService.saveCompetition(competition);
		model.addAttribute(competition);
		InvitationHelper invitationHelper = new InvitationHelper();
		invitationHelper.setCompetitionId(competitionId);
		model.addAttribute(invitationHelper);
    	model.addAttribute("groupList", getUserService().getUserGroups(getUserId()));
    	model.addAttribute("communityList", getUserService().getUserCommunities(getUserId()));
    	model.addAttribute("friendList", getUserProfile().getFriends());
		logUserAction("Confirm " + competition);
    	updateCompetitionCounts(session);
		return "inviteToCompetitionView";
	}
	
	@RequestMapping(value="/competitioninviteview")	
	public String getCompetitionInviteView(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		model.addAttribute(competition);
		InvitationHelper invitationHelper = new InvitationHelper();
		invitationHelper.setCompetitionId(competitionId);
		model.addAttribute(invitationHelper);
		model.addAttribute("groupList", getUserService().getUserGroups(getUserId()));
		model.addAttribute("communityList", getUserService().getUserCommunities(getUserId()));
		model.addAttribute("friendList", getUserProfile().getFriends());
		
		return "inviteToCompetitionView";
	}
	
	@RequestMapping(value="/competitionalternatives")	
	public String getCompetitionAlternativesView(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		Alternative alternative = new Alternative();
		alternative.setEventId(competition.getDefaultEvent().getId());
		alternative.setCompetitionId(competition.getId());
		model.addAttribute(alternative);
		model.addAttribute("alternativesList", competition.getSortedAlternatives());

		return "createAlternativeView";
	}
	
	@RequestMapping(value="/deletealternative")	
	public String deleteAlternative(@RequestParam("competitionId") Long competitionId, @RequestParam("alternativeId") Long alternativeId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		
		Set<Alternative> alternatives = competition.getDefaultEvent().getAlternatives();
		Set<Alternative> filteredAlternatives = new HashSet<Alternative>();
		for (Alternative alternative : alternatives ) {
			if (alternative.getId().compareTo(alternativeId) != 0) {
				filteredAlternatives.add(alternative);
			}
		}
		competition.getDefaultEvent().setAlternatives(filteredAlternatives);
		competition.getDefaultEvent().normalizeAlternativesPriorities();
		competition = competitionService.saveCompetition(competition);
		
		Alternative alternative = new Alternative();
		alternative.setEventId(competition.getDefaultEvent().getId());
		alternative.setCompetitionId(competition.getId());
		model.addAttribute(alternative);
		model.addAttribute("alternativesList", competition.getSortedAlternatives());
		
		return "createAlternativeView";
	}
	
	@RequestMapping(value="/movealternative")	
	public String moveAlternative(@RequestParam("competitionId") Long competitionId, @RequestParam("alternativeId") Long alternativeId, @RequestParam("direction") String direction, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		Alternative currentAlternative = competition.getAlternativeById(alternativeId);
		Alternative previousAlternative = competition.getDefaultEvent().getPreviousAlternativeInList(currentAlternative);
		Alternative nextAlternative = competition.getDefaultEvent().getNextAlternativeInList(currentAlternative);
		
        if (direction.equalsIgnoreCase("up") && previousAlternative != null) {
        	currentAlternative.decreasePriority();
            previousAlternative.increasePriority();
        } else if (direction.equalsIgnoreCase("down") && nextAlternative != null) {
        	currentAlternative.increasePriority();
            nextAlternative.decreasePriority();
        }
		
		competition = competitionService.saveCompetition(competition);
		
		Alternative alternative = new Alternative();
		alternative.setEventId(competition.getDefaultEvent().getId());
		alternative.setCompetitionId(competition.getId());
		model.addAttribute(alternative);
		model.addAttribute("alternativesList", competition.getDefaultEvent().getSortedAlternatives());
		
		return "createAlternativeView";
	}
	
	@RequestMapping(value="/savecompetition")	
	public String saveCompetition(@Valid Competition competition, BindingResult result, HttpSession session, Model model) {
		boolean goToNextStep = competition.isGoToNextStep();
    	if (result.hasErrors()) {
    		model.addAttribute(result.getAllErrors());
    		return "createCompetitionView";
    	}
    	
    	competition.setOwnerId(getUserId());
    	Account account = accountService.getAccount(competition.getAccountId());
    	competition.setCurrency(account.getCurrency());
    	MultipartFile imageFile = competition.getImageFile();
    	
    	Long competitionId = competition.getId();
    	
    	if (competitionId != null) {
    		Competition storedCompetition = competitionService.getCompetitionById(competition.getId());
    		competition.setEvents(storedCompetition.getEvents()); //TODO: Refactor this
    	} 

    	competition.setStatus(CompetitionStatus.NEW);
    	competition = competitionService.saveCompetition(competition);
    	saveImage(imageFile, IMAGE_FOLDER_COMPETITIONS, competition.getId().toString());
    	
    	if (competition.getEvents() == null || competition.getEvents().isEmpty()) {
    		logger.debug("Adding default event to competition");
    		Event event = new Event(competition.getName());
    		competition.addEvent(event);
        	competition = competitionService.saveCompetition(competition);
    	}

    	logUserAction("Saved " + competition);
    	
    	if (goToNextStep) {
    		Alternative alternative = new Alternative();
    		alternative.setEventId(competition.getDefaultEvent().getId());
    		alternative.setCompetitionId(competition.getId());
    		model.addAttribute(alternative);
    		model.addAttribute("alternativesList", competition.getSortedAlternatives());

    		return "createAlternativeView";
    	}

    	updateCompetitionCounts(session);
		return "userHomepageAction";
	}

	@RequestMapping(value="/savetempcompetitionimage")	
	public void saveTempImage(@RequestParam("imageFile") MultipartFile imageFile, HttpServletResponse response) {
		String filename = "tmp" + getUserId();
		boolean success = saveImage(imageFile, IMAGE_FOLDER_COMPETITIONS, filename);
		if (success) {
			String message = "{\"success\":\"true\", \"filename\":\"" + filename + "\"}";
			sendResponseStatusAndMessage(response, HttpServletResponse.SC_OK, message);
		} else {
			String message = "{\"success\":\"false\", \"filename\":\"" + filename + "\"}";
			sendResponseStatusAndMessage(response, HttpServletResponse.SC_OK, message);
		}
	}
	
	@RequestMapping(value="/invitetocompetition")	
	public String inviteToCompetition(@ModelAttribute("invitationHelper") InvitationHelper invitationHelper, BindingResult result, Model model) {
		if (result.hasErrors()) {
			logger.debug("Error found: " + result.getErrorCount());
			return "inviteToCompetitionView";
		}
		
		Competition competition = competitionService.getCompetitionById(invitationHelper.getCompetitionId());

		if (invitationHelper.shouldInviteToFacebook()) {
			facebookService.postCompetitionToUserWall(competition, getUserProfile());
		} else {
	    	Set<Long> friendsIdSet = new HashSet<Long>();
	    	if (invitationHelper.isAllFriends()) {
	    		friendsIdSet.addAll(getUserProfile().getFriendsIdSet());
	    	} else {
	    		friendsIdSet.addAll(invitationHelper.getFriendsIdSet());
	    		Set<Long> groupIdSet = invitationHelper.getGroupIdSet();
	    		for (Long groupId : groupIdSet) {
	    			Group group = getUserService().getGroupById(groupId);
	    			friendsIdSet.addAll(group.getMembersIdSet());
	    		}
	    	}
	    	
	    	for (Long communityId : invitationHelper.getCommunityIdSet()) {
	    		Community community = getUserService().getCommunityById(communityId);
	    		friendsIdSet.addAll(community.getMembersIdSet());
	    	}
	
	    	competitionService.sendInvitationsToFriends(competition, friendsIdSet, getUserProfile());
		}
    	
    	Activity activity = new Activity(getUserProfile(), ActivityType.USER);
    	activity.setMessage("Created new competition: " + competition.getName());
    	activityService.saveActivity(activity);
		
		return "manageCompetitionsAction";
	}
	
	@RequestMapping(value="/savealternative")	
	public String saveAlternative(@Valid Alternative alternative, BindingResult result, Model model) {
		if (result.hasErrors()) {
			logger.debug("Error found: " + result.getErrorCount());
			return "createAlternativeView";
		}
		
		MultipartFile imageFile = alternative.getImageFile();
		Long eventId = alternative.getEventId();
		Long competitionId = alternative.getCompetitionId();
		
		alternative.setAlternativeType(AlternativeType.CUSTOM);
		alternative = competitionService.saveAlternative(alternative);
		saveImage(imageFile, IMAGE_FOLDER_COMPETITIONS, "alt_" + alternative.getId());

		Event event = competitionService.getEventById(eventId);
		event.addAlternative(alternative);
		event = competitionService.saveEvent(event);
		
		alternative = new Alternative();
		alternative.setEventId(eventId);
		alternative.setCompetitionId(competitionId);
		model.addAttribute(alternative);
		model.addAttribute("alternativesList", event.getSortedAlternatives());
		return "createAlternativeView";
	}
	
    @RequestMapping(value="/join/{competitionHash}")
    protected String viewUserProfile(@PathVariable("competitionHash") String competitionHash, ModelMap model) {
    	if (Base64.isBase64(competitionHash.getBytes())) {
    		String decodedString = new String(Base64.decode(competitionHash.getBytes()));
    		String competitionIdString = decodedString.substring(decodedString.indexOf("/") + 1);
    		logger.info("Join competition link activated for competition: " + competitionIdString);
    		try {
    			Long competitionId = new Long(competitionIdString);
    			Competition competition = competitionService.getCompetitionById(competitionId);
    			model.addAttribute("competition", competition);
    		} catch (NumberFormatException ex) {
    			logger.error("Unparsable competition id in request: " + competitionIdString);
    		}
    	} else {
    		logger.error("Competition id in link is not encoded: " + competitionHash);
    	}
    	
        return "joinCompetitionView";
    }
	
	@RequestMapping(value="/quickcompetitionview")	
	public String getView(@RequestParam("accountId") Long accountId, @RequestParam("stake") BigDecimal stake, @RequestParam("alternative") String alternative, Model model) {
		QuickCompetition competition = new QuickCompetition();
		competition.setName(alternative);
		competition.setStake(stake);
		competition.setAccountId(accountId);
		
		DateTime now = new DateTime();
		competition.setDeadline(now.plusDays(competitionService.getDefaultDeadlineInterval()).toDate());
		competition.setSettlingDeadline(now.plusDays(competitionService.getDefaultSettlingInterval()).toDate());
		
    	model.addAttribute("quickCompetition", competition);
		
		return "quickCompetitionView";
	}
	
	@RequestMapping(value="/quickcompetition")	
	public String processSubmit(@Valid QuickCompetition quickCompetition, BindingResult result, HttpSession session, Model model) {
    	if (result.hasErrors()) {
    		model.addAttribute(result.getAllErrors());
    		return "quickCompetitionView";
    	}
    	
    	Account account = accountService.getAccount(quickCompetition.getAccountId());
    	Competition competition = quickCompetition.createCompetition(getUserId(), account.getCurrency());
    	competition = competitionService.saveCompetition(competition);
    	logUserAction("Created " + competition);
    	saveImage(quickCompetition.getImageFile(), IMAGE_FOLDER_COMPETITIONS, competition.getId().toString());
    	
    	Bet bet = quickCompetition.createBet(getUserProfile());
    	bet.setSelectionId(competition.getOwnerAlternativeId());
    	competitionService.placeBet(bet);
    	logUserAction("Placed " + bet);
    	
		model.addAttribute(competition);
		InvitationHelper invitationHelper = new InvitationHelper();
		invitationHelper.setCompetitionId(competition.getId());
		model.addAttribute(invitationHelper);
    	model.addAttribute("groupList", getUserService().getUserGroups(getUserId()));
    	model.addAttribute("communityList", getUserService().getUserCommunities(getUserId()));
    	model.addAttribute("friendList", getUserProfile().getFriends());
		
    	updateCompetitionCounts(session);
    	
		return "inviteToCompetitionView";
	}

	@RequestMapping(value="/placebet")
	public String placeBet(@RequestParam("invitationId") Long invitationId, 
			@RequestParam("alternativeId") Long alternativeId, 
			@RequestParam(value="stake", required=false) BigDecimal stake, Model model) {
		
		Invitation invitation = competitionService.getInvitationById(invitationId);
		Competition competition = competitionService.getCompetitionById(invitation.getCompetitionId());

    	Bet bet = new Bet(getUserProfile(), competition);
    	bet.setSelectionId(alternativeId);
    	
		BigDecimal validStake = getValidStake(competition, stake);
		if (validStake == null) {
			model.addAttribute("invalidStake", true);
			model.addAttribute("invitationId", invitationId);
			return "invitationAction";
		}
		bet.setStake(validStake);
    	
    	competitionService.placeBet(bet);
    	competitionService.deleteInvitation(invitationId);
    	logUserAction("Joined " + competition);
    	logUserAction("Placed " + bet);
    	
    	Activity activity = new Activity(getUserProfile(), ActivityType.USER);
    	activity.setMessage("Joined the competition: " + competition.getName());
    	
    	activityService.saveActivity(activity);

		return "userHomepageAction";
	}
	
	@RequestMapping(value="/placeanotherbet")
	public String placeAnotherBet(@RequestParam("alternativeId") Long alternativeId, 
			@RequestParam("competitionId") Long competitionId, 
			@RequestParam(value="stake", required=false) BigDecimal stake, Model model) {
	
		Competition competition = competitionService.getCompetitionById(competitionId);
		
		Bet bet = new Bet(getUserProfile(), competition);
		bet.setSelectionId(alternativeId);
		
		BigDecimal validStake = getValidStake(competition, stake);
		if (validStake == null) {
			model.addAttribute("invalidStake", true);
			model.addAttribute("competitionId", competitionId);
			return "ongoingCompetitionAction";
		}
		bet.setStake(validStake);

		competitionService.placeBet(bet);
    	logUserAction("Placed " + bet);
		
		return "userHomepageAction";
	}
	
	private BigDecimal getValidStake(Competition competition, BigDecimal stake) {
		if (competition.getCompetitionType() != CompetitionType.POOL_BETTING) {
			stake = competition.getFixedStake();
		} 
		
		Account account = accountService.getUserAccountForCurrency(getUserId(), competition.getCurrency());
		return account.isValidStake(stake) ? stake : null;
	}
	
	@RequestMapping(value="/competition/images/{competitionId}")	
	public void getImage(@PathVariable String competitionId, HttpServletResponse response) {
    	sendJPEGImage(IMAGE_FOLDER_COMPETITIONS, competitionId, response);
	}
	
    private void updateCompetitionCounts(HttpSession session) {
    	session.setAttribute("myCompetitionsCount", competitionService.getActiveCompetitionsByUserCount(getUserId()));
    	session.setAttribute("mySettledCompetitionCount", competitionService.getSettledCompetitionsByUserCount(getUserId()));
    	session.setAttribute("myNewCompetitionCount", competitionService.getNewCompetitionsByUserCount(getUserId()));
    	Integer ongoingCompetitionCount = competitionService.getOngoingCompetitionsByUserCount(getUserId());
    	session.setAttribute("myOngoingCompetitionsCount", ongoingCompetitionCount);
    	Integer invitationsCount = competitionService.getInvitationsForUserCount(getUserId());
    	session.setAttribute("myInvitationsCount", new Integer(ongoingCompetitionCount.intValue() + invitationsCount.intValue()));
    }

}
