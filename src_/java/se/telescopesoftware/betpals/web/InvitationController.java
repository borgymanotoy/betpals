package se.telescopesoftware.betpals.web;

import java.math.BigDecimal;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.Event;
import se.telescopesoftware.betpals.domain.Invitation;
import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.ActivityService;
import se.telescopesoftware.betpals.services.CompetitionService;

@Controller
public class InvitationController extends AbstractCompetitionController {

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

	@RequestMapping(value="/invitations")
	public String showActiveInvitations(Model model) {

		model.addAttribute("invitationList", competitionService.getInvitationsForUser(getUserId()));
		model.addAttribute("competitionList", competitionService.getOngoingCompetitionsByUser(getUserId()));

		return "activeInvitationsView";
	}

	@RequestMapping(value="/invitation")
	public String viewInvitation(@RequestParam("invitationId") Long invitationId, Model model) {
		Invitation invitation = competitionService.getInvitationById(invitationId);
		Competition competition = competitionService.getCompetitionById(invitation.getCompetitionId());
		for(Event e : competition.getEvents()){
			e.setAlternatives(sortAlternativeSet(e.getSortedAlternatives()));
		}
		model.addAttribute("invitation", invitation);
		model.addAttribute("competition", competition);
		return "invitationView";
	}

	@RequestMapping(value="/linkinvitation")
	public String viewLinkInvitation(@RequestParam("competitionId") Long competitionId, @RequestParam(value="alternativeId", defaultValue="0", required=false) Long alternativeId, @RequestParam(value="betValue", defaultValue="0", required=false) BigDecimal betValue, Model model, HttpSession session) {

		Competition competition = competitionService.getCompetitionById(competitionId);
		Invitation invitation = new Invitation(competition, getUserId());

		for(Event e : competition.getEvents()){
			e.setAlternatives(sortAlternativeSet(e.getSortedAlternatives()));
		}

		model.addAttribute("invitation", invitation);
		model.addAttribute("competition", competition);

		model.addAttribute("alternativeId", alternativeId);
		model.addAttribute("betValue", betValue.toString());

		setLeftRightPaneContents(model, session);

		return "invitationView";
	}

    /** Sets the ongoing competition page contents
     *  <p>
     *  This method will simply set the data to be displayed in the left pane, right pane as well as the ongoing competition page.
     *  @param activities  The collection of competition activities like the bets placed by punters.
     *  @param pageId  The current page in the ongoing competition page.
     *  @param model  Spring Framework UI Model
     *  @param session  The HTTP Session
     */
    public void setLeftRightPaneContents(Model model, HttpSession session) {
        setLeftPaneData1(model, session);
        setRightPaneData1(session);

        setLeftPaneData(model, session);
        setRightPaneData(session);
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
