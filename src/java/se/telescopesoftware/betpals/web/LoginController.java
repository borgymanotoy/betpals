package se.telescopesoftware.betpals.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import se.telescopesoftware.betpals.services.UserService;

@Controller
public class LoginController {
	
	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping("/login")
	public String get() {
		return "loginView";
	}

	@RequestMapping("/index")
	public String getIndex() {
		return "userHomepageAction";
	}

	@RequestMapping("/")
	public String getRoot() {
		return "userHomepageAction";
	}
	
}
