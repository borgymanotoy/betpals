package se.telescopesoftware.betpals.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.UserService;


@Controller
@SessionAttributes({"user", "friendsSideList"})
public class HomeController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

	@RequestMapping("/home")
	public String get(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	User user = (User) authentication.getPrincipal();
    	UserProfile userProfile = user.getUserProfile();

    	Collection<UserProfile> friends = userService.getUserFriends(user.getId());
    	model.addAttribute("friendsSideList", friends);    	
		model.addAttribute("user", userProfile);
		return "userHomepageView";
	}
}
