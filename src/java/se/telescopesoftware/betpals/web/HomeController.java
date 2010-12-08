package se.telescopesoftware.betpals.web;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.ActivityService;
import se.telescopesoftware.betpals.services.UserService;


@Controller
public class HomeController extends AbstractPalsController {

    private UserService userService;
    private ActivityService activityService;
    private AccountService accountService;

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
    

	@RequestMapping("/home")
	public String get(Model model, HttpSession session) {
    	Collection<UserProfile> friends = userService.getUserFriends(getUserId());
    	Collection<Activity> activities = activityService.getActivitiesForUserProfile(getUserProfile());
    	Collection<Account> accounts = accountService.getUserAccounts(getUserId());
    	
    	session.setAttribute("friendsSideList", friends);    	
    	session.setAttribute("user", getUserProfile());
    	session.setAttribute("accounts", accounts);
    	model.addAttribute("activitiesList", activities);    	
		
		return "userHomepageView";
	}
}
