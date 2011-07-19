package se.telescopesoftware.betpals.web.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.CompetitionService;
import se.telescopesoftware.betpals.web.AbstractPalsController;


@Controller
public class UsersController extends AbstractPalsController {

    private AccountService accountService;
    private CompetitionService competitionService;


    @Autowired
    public void setAccountService(AccountService accountService) {
    	this.accountService = accountService;
    }
    
    @Autowired
    public void setCompetitionService(CompetitionService competitionService) {
    	this.competitionService = competitionService;
    }
    
    
	@RequestMapping("/admin/listusers")
	public String listUsers(Model model) {
    	Collection<UserProfile> users = getUserService().getAllUserProfiles(getUser(), null, null);
    	populateStatistics(users);
    	model.addAttribute("userList", users);
		return "userListView";
	}

    @RequestMapping(value="/admin/searchusers")
    public String searchUsers(@RequestParam("query") String query, Model model) {
    	Collection<UserProfile> users = getUserService().searchUserProfiles(query, getUserId());
    	populateStatistics(users);
    	model.addAttribute("userList", users);
    	return "userListView";
    }
	   
	@RequestMapping("/admin/viewuser")
	public String viewUser(@RequestParam("userId") Long userProfileId, Model model) {
		UserProfile userProfile = getUserService().getUserProfile(userProfileId);
    	Collection<Account> accounts = accountService.getUserAccounts(userProfile.getUserId());
    	populateStatistics(userProfile);
		model.addAttribute(userProfile);
		model.addAttribute("userAccountList", accounts);
		model.addAttribute("userLog", getUserService().getUserLogEntries(userProfile.getUserId()));
		return "userView";
	}
	
	@RequestMapping("/admin/blockuser")
	public String blockUser(@RequestParam("userId") Long userId, Model model) {
		getUserService().blockUser(userId);
		UserProfile userProfile = getUserService().getUserProfileByUserId(userId);

		return viewUser(userProfile.getId(), model);
	}
	
	
	private void populateStatistics(Collection<UserProfile> users) {
		for (UserProfile userProfile : users) {
			populateStatistics(userProfile);
		}
	}

	private void populateStatistics(UserProfile userProfile) {
		userProfile.setCompetitionsCount(competitionService.getTotalUserCompetitionsCount(userProfile.getUserId()));
		userProfile.setBetsCount(competitionService.getTotalUserBetsCount(userProfile.getUserId()));
		userProfile.setLastCompetitionDate(competitionService.getLastCompetitionCreatedDate(userProfile.getUserId()));
		userProfile.setLastBetDate(competitionService.getLastBetPlacedDate(userProfile.getUserId()));
	}
	
}
