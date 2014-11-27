package se.telescopesoftware.betpals.web;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.Alternative;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.Event;
import se.telescopesoftware.betpals.services.AccountService;
//import se.telescopesoftware.betpals.services.ActivityService;
//import se.telescopesoftware.betpals.services.CompetitionService;

@Controller
public abstract class AbstractCompetitionController extends AbstractPalsController {

	//private CompetitionService competitionService;
    //private ActivityService activityService;
    private AccountService accountService;
    /*
	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}
    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }
    */
    @Autowired
    public void setAccountService(AccountService accountService) {
    	
    	logger.info("\n\n\n");
    	logger.info("<< SET-ACCOUNT-SERVICE >>");
    	logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    	logger.info("[ACCOUNT-SERVICE]: " + accountService);
    	logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    	logger.info("\n\n\n");
    	
        this.accountService = accountService;
    }

    protected List<Alternative> getSortedAlternativeList(Competition competition) {
        List<Alternative> alternativeSet = null;
        if (competition != null) {
            Event e = competition.getDefaultEvent();
            if (e != null) {
                 alternativeSet = e.getSortedAlternatives();
            }
        }
        return alternativeSet;
    }
    /** Returns a sorted alternative set that will be used in the JSP(view).
     *  <p>
     *  This method returns the sorted alternative set whether the alternative list is sorted or not.
     *  @param list  the alternative list to be converted into a sorted alternative set.
     *  @return The sorted alternative set.
     */
    protected Set<Alternative> sortAlternativeSet(List<Alternative> list){
        Set<Alternative> set = new TreeSet<Alternative>(list);
        return set;
    }
    
    /** Sets the left pane data
     *  <p>
     *  This method will simply set the data to be displayed in the left pane.
     *  @param model  Spring Framework UI Model
     *  @param session  The HTTP Session
     */
    protected void setLeftPaneData1(Model model, HttpSession session) {
    	
    	logger.info("\n\n\n");
    	logger.info("<< SET-LEFT-PANE-DATA-1 >>");
    	logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    	logger.info("[USER-ID]: " + getUserId());
    	logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    	logger.info("\n\n\n");
    	
    	//Collection<Account> accounts = null;
    	//if(accountService != null) accounts = accountService.getUserAccounts(getUserId());

    	Collection<Account> accounts = accountService.getUserAccounts(getUserId());
    	
        
        session.setAttribute("friendsSideList", getUserProfile().getLastLoggedInFriends());
        session.setAttribute("user", getUserProfile());
        //session.setAttribute("accounts", accounts);
        session.setAttribute("myRequestsCount", getUserService().getUserRequestForUserCount(getUserId()));
        //session.setAttribute("myCompetitionsCount", competitionService.getActiveCompetitionsByUserCount(getUserId()));
        //session.setAttribute("mySettledCompetitionCount", competitionService.getSettledCompetitionsByUserCount(getUserId()));
        //session.setAttribute("myNewCompetitionCount", competitionService.getNewCompetitionsByUserCount(getUserId()));
        //Integer ongoingCompetitionCount = competitionService.getOngoingCompetitionsByUserCount(getUserId());
        //session.setAttribute("myOngoingCompetitionsCount", ongoingCompetitionCount);
        //Integer invitationsCount = competitionService.getInvitationsForUserCount(getUserId());
        //session.setAttribute("myInvitationsCount", new Integer(ongoingCompetitionCount.intValue() + invitationsCount.intValue()));
        //model.addAttribute("numberOfPages", activityService.getActivitiesPageCountForUserProfile(getUserProfile(), null));

    }

    /** Sets the right pane data or the top public competitions displayed in the right pane
     *  <p>
     *  This method will simply set the data to be displayed in the right pane, which is the top public competitions.
     *  @param session  The HTTP Session
     */
    protected void setRightPaneData1(HttpSession session) {
    	
    	logger.info("\n\n\n");
    	logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    	logger.info("<< SET-RIGHT-PANE-DATA-1 >>");
    	logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    	logger.info("\n\n\n");
    	
    	/*
    	session.setAttribute("topPublicCompetitions", competitionService.getTopPublicCompetitionsByTurnover(5));
    	*/
    }
}