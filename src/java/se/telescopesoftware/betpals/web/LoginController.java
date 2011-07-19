package se.telescopesoftware.betpals.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;

@Controller
public class LoginController extends AbstractPalsController {
	
	@RequestMapping("/login")
	public String get(Model model) {
		model.addAttribute(new UserProfile());
		return "loginView";
	}

	@RequestMapping({"/", "/index"})
	public String getRoot() {
		User user = getUser();
		if (user.isSupervisor()) {
			return "adminHomepageAction";
		}
		
		return "userHomepageAction";
	}
	
	@RequestMapping("/processlogin.html")
	public String processLogin() {
		UserProfile userProfile = getUserProfile();
		userProfile.registerVisit();
		getUserService().updateUserProfile(userProfile);
		logger.info("Logged in " + getUser());
		logUserAction("Log in");
		return "indexAction";
	}

}
