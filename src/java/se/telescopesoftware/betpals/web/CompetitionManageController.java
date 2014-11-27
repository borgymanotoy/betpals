package se.telescopesoftware.betpals.web;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.AccessType;
import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityType;
import se.telescopesoftware.betpals.domain.Alternative;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.CompetitionStatus;
import se.telescopesoftware.betpals.domain.Event;
import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.ActivityService;
import se.telescopesoftware.betpals.services.CompetitionService;

@Controller
public class CompetitionManageController extends AbstractCompetitionController {
	
	private CompetitionService competitionService;
    private ActivityService activityService;
    private AccountService accountService;
    
	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

	@RequestMapping(value="/managecompetitions")	
	public String getCompetitionsListView(Model model, HttpSession session) {

		model.addAttribute("competitionList", competitionService.getActiveCompetitionsByUser(getUserId()));
		model.addAttribute("settledCompetitionCount", competitionService.getSettledCompetitionsByUserCount(getUserId()));
    	session.setAttribute("myCompetitionsCount", competitionService.getActiveCompetitionsByUserCount(getUserId()));

		return "manageCompetitionsView";
	}
	
	@RequestMapping(value="/settledcompetitions")	
	public String getSettledCompetitionsListView(Model model, HttpSession session) {
		
		model.addAttribute("competitionList", competitionService.getSettledCompetitionsByUser(getUserId()));
		return "settledCompetitionsView";
	}

	@RequestMapping(value="/newcompetitions")	
	public String getNewCompetitionsListView(Model model, HttpSession session) {
		model.addAttribute("competitionList", competitionService.getNewCompetitionsByUser(getUserId()));
		return "newCompetitionListView";
	}
	
	@RequestMapping(value="/settledcompetition")	
	public String getSettledCompetitionView(@RequestParam("competitionId") Long competitionId, @RequestParam(value="pageId", defaultValue="0", required=false) Integer pageId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		for(Event e : competition.getEvents()){
			e.setAlternatives(sortAlternativeSet(e.getSortedAlternatives()));
		}
		model.addAttribute(competition);
		Collection<Activity> activities = activityService.getActivitiesForExtensionIdAndType(competitionId, pageId, null, ActivityType.COMPETITION);
		model.addAttribute(activities);
    	model.addAttribute("numberOfPages", activityService.getActivitiesPageCountForExtensionIdAndType(competitionId, ActivityType.COMPETITION, null));

		return "settledCompetitionView";
	}
	
	
	@RequestMapping(value="/ongoingcompetitions")	
	public String getOngoingCompetitionsListView(Model model, HttpSession session) {
		model.addAttribute("competitionList", competitionService.getOngoingCompetitionsByUser(getUserId()));
		
		return "ongoingCompetitionsView";
	}
	
	@RequestMapping(value="/publiccompetitions")	
	public String getPublicCompetitionsListView(@RequestParam(value="pageId", defaultValue="0", required=false) Integer pageId, Model model, HttpSession session) {
		model.addAttribute("competitionList", competitionService.getActiveCompetitionsByAccessType(pageId, null, AccessType.PUBLIC));
    	model.addAttribute("currentPage", pageId);
    	model.addAttribute("numberOfPages", competitionService.getCompetitionPageCountForAccessType(AccessType.PUBLIC, null));

		return "publicCompetitionsView";
	}
	
    @RequestMapping(value="/searchpubliccompetitions")
    public String searchPublicCompetitions(@RequestParam("query") String query, Model model) {
    	model.addAttribute("competitionList", competitionService.searchPublicCompetitions(query));
    	return "publicCompetitionsView";
    }
	
	@RequestMapping(value="/ongoingcompetition")	
	public String getOngoingCompetitionView(@RequestParam("competitionId") Long competitionId, @RequestParam(value="pageId", defaultValue="0", required=false) Integer pageId, Model model, HttpServletRequest request, HttpSession session) {
        String altId = (String) request.getParameter("alternativeId");
        String betValue = (String) request.getParameter("betValue");

        altId = (altId != null) ? altId : "0";
        betValue = (betValue != null) ? betValue : "";

        Long alternativeId = new Long(altId);

        model.addAttribute("alternativeId", alternativeId);
        model.addAttribute("betValue", betValue);

		Competition competition = competitionService.getCompetitionById(competitionId); //TODO: Check user permissions to view competition
		model.addAttribute(competition);
		model.addAttribute("sortedAlternativeList", getSortedAlternativeList(competition));

		Collection<Activity> activities = activityService.getActivitiesForExtensionIdAndType(competitionId, pageId, null, ActivityType.COMPETITION);
		model.addAttribute(activities);
    	model.addAttribute("numberOfPages", activityService.getActivitiesPageCountForExtensionIdAndType(competitionId, ActivityType.COMPETITION, null));

		setOngoingCompetitionContents(activities, pageId, model, session);

		return "ongoingCompetitionView";
	}
	
	@RequestMapping(value="/managecompetition")	
	public String getManageCompetitionView(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		for(Event e : competition.getEvents()){
			e.setAlternatives(sortAlternativeSet(e.getSortedAlternatives()));
		}
		model.addAttribute(competition);
		
		return "manageCompetitionView";
	}
	
	@RequestMapping(value="/deletecompetition")	
	public String deleteCompetition(@RequestParam("competitionId") Long competitionId, HttpSession session, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		logUserAction("Deleted " + competition);
		competitionService.deleteCompetition(competitionId);
		updateCompetitionCounts(session);
		return "manageCompetitionsAction";
	}
	
	@RequestMapping(value="/voidalternative")	
	public String voidAlternative(@RequestParam("competitionId") Long competitionId, @RequestParam("alternativeId") Long alternativeId, Locale locale, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		Alternative alternative = competition.getAlternativeById(alternativeId);
		logUserAction("Voiding " + alternative);
		competitionService.voidAlternative(competitionId, alternativeId, locale);
		
		competition = competitionService.getCompetitionById(competitionId);
		for(Event e : competition.getEvents()){
			e.setAlternatives(sortAlternativeSet(e.getSortedAlternatives()));
		}
		model.addAttribute(competition);

		return "manageCompetitionView";
	}
	
	@RequestMapping(value="/settlecompetition")	
	public String settleCompetition(@RequestParam("competitionId") Long competitionId, @RequestParam("alternativeId") Long alternativeId, HttpSession session, Locale locale, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		logUserAction("Settle " + competition);
		competitionService.settleCompetition(competitionId, alternativeId, locale);
		
		updateCompetitionCounts(session);
		return "manageCompetitionsAction";
	}
	
	@RequestMapping(value="/closecompetition")	
	public String closeCompetition(@RequestParam("competitionId") Long competitionId, HttpSession session, Locale locale, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		competition.setStatus(CompetitionStatus.CLOSE);
		competition.setDeadline(new Date());
		competitionService.saveCompetition(competition, locale, true);
		logUserAction("Close " + competition);
		updateCompetitionCounts(session);
		return "manageCompetitionsAction";
	}
	
	@RequestMapping(value="/updatecompetition")	
	public String inviteToCompetition(@ModelAttribute("competition") Competition competition, BindingResult result, Locale locale, Model model) {
		if (result.hasErrors()) {
			logger.debug("Error found: " + result.getErrorCount());
			return "manageCompetitionView";
		}
		
		Competition storedCompetition = competitionService.getCompetitionById(competition.getId());
		storedCompetition.setDeadline(competition.getDeadline());
		storedCompetition.setSettlingDeadline(competition.getSettlingDeadline());
		competitionService.saveCompetition(storedCompetition, locale, true);
		logUserAction("Update " + competition);
		
		return "manageCompetitionsAction";
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

    /** Sets the ongoing competition page contents
     *  <p>
     *  This method will simply set the data to be displayed in the left pane, right pane as well as the ongoing competition page.
     *  @param activities  The collection of competition activities like the bets placed by punters.
     *  @param pageId  The current page in the ongoing competition page.
     *  @param model  Spring Framework UI Model
     *  @param session  The HTTP Session
     */
    private void setOngoingCompetitionContents(Collection<Activity> activities, Integer pageId, Model model, HttpSession session) {
        setLeftPaneData(model, session);
        setRightPaneData(session);

        model.addAttribute("activitiesList", activities);
        model.addAttribute("currentPage", pageId);
        model.addAttribute("numberOfPages", activityService.getActivitiesPageCountForUserProfile(getUserProfile(), null));
    }

    /** Sets the left pane data
     *  <p>
     *  This method will simply set the data to be displayed in the left pane.
     *  @param model  Spring Framework UI Model
     *  @param session  The HTTP Session
     */
    private void setLeftPaneData(Model model, HttpSession session) {
        Collection<Account> accounts = accountService.getUserAccounts(getUserId());
        session.setAttribute("friendsSideList", getUserProfile().getLastLoggedInFriends());
        session.setAttribute("user", getUserProfile());
        session.setAttribute("accounts", accounts);
        session.setAttribute("myRequestsCount", getUserService().getUserRequestForUserCount(getUserId()));
        session.setAttribute("myCompetitionsCount", competitionService.getActiveCompetitionsByUserCount(getUserId()));
        session.setAttribute("mySettledCompetitionCount", competitionService.getSettledCompetitionsByUserCount(getUserId()));
        session.setAttribute("myNewCompetitionCount", competitionService.getNewCompetitionsByUserCount(getUserId()));
        Integer ongoingCompetitionCount = competitionService.getOngoingCompetitionsByUserCount(getUserId());
        session.setAttribute("myOngoingCompetitionsCount", ongoingCompetitionCount);
        Integer invitationsCount = competitionService.getInvitationsForUserCount(getUserId());
        session.setAttribute("myInvitationsCount", new Integer(ongoingCompetitionCount.intValue() + invitationsCount.intValue()));
        model.addAttribute("numberOfPages", activityService.getActivitiesPageCountForUserProfile(getUserProfile(), null));
    }

    /** Sets the right pane data or the top public competitions displayed in the right pane
     *  <p>
     *  This method will simply set the data to be displayed in the right pane, which is the top public competitions.
     *  @param session  The HTTP Session
     */
    private void setRightPaneData(HttpSession session) {
        session.setAttribute("topPublicCompetitions", competitionService.getTopPublicCompetitionsByTurnover(5));
    }
}
