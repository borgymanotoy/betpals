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
import se.telescopesoftware.betpals.services.UserService;
import se.telescopesoftware.betpals.web.AbstractPalsController;


@Controller
public class UsersController extends AbstractPalsController {

    private UserService userService;
    private AccountService accountService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
    	this.accountService = accountService;
    }
    
    
	@RequestMapping("/admin/listusers")
	public String listUsers(Model model) {
    	Collection<UserProfile> users = userService.getAllUserProfiles(getUser(), null, null);
    	model.addAttribute("userList", users);
		return "userListView";
	}

	@RequestMapping("/admin/viewuser")
	public String viewUser(@RequestParam("userId") Long userProfileId, Model model) {
		UserProfile userProfile = userService.getUserProfile(userProfileId);
    	Collection<Account> accounts = accountService.getUserAccounts(userProfile.getUserId());
    	
		model.addAttribute(userProfile);
		model.addAttribute("userAccountList", accounts);
		return "userView";
	}
	
	@RequestMapping("/admin/blockuser")
	public String blockUser(@RequestParam("userId") Long userId, Model model) {
		userService.blockUser(userId);
		UserProfile userProfile = userService.getUserProfileByUserId(userId);

		return viewUser(userProfile.getId(), model);
	}
	
}
