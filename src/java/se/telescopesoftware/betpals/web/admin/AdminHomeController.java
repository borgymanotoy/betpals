package se.telescopesoftware.betpals.web.admin;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.ActivityService;
import se.telescopesoftware.betpals.services.CompetitionService;
import se.telescopesoftware.betpals.services.UserService;
import se.telescopesoftware.betpals.web.AbstractPalsController;


@Controller
public class AdminHomeController extends AbstractPalsController {

    private UserService userService;
    private ActivityService activityService;
    private AccountService accountService;
    private CompetitionService competitionService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

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