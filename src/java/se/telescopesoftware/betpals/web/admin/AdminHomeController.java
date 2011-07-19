package se.telescopesoftware.betpals.web.admin;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.ActivityService;
import se.telescopesoftware.betpals.services.CompetitionService;
import se.telescopesoftware.betpals.web.AbstractPalsController;


@Controller
public class AdminHomeController extends AbstractPalsController {

    private ActivityService activityService;
    private AccountService accountService;
    private CompetitionService competitionService;

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }
    
    @Autowired
    public void setAccountService(AccountService accountService) {
    	this.accountService = accountService;
    }
    
    @Autowired
    public void setCompetitionService(CompetitionService competitionService) {
    	this.competitionService = competitionService;
    }

	@RequestMapping("/adminhomepage")
	public String get(Model model, HttpSession session) {
    	
    	session.setAttribute("user", getUserProfile());
		return "adminHomepageView";
	}

}
