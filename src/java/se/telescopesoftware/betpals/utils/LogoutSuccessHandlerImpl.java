package se.telescopesoftware.betpals.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserLogEntry;
import se.telescopesoftware.betpals.services.UserService;

public class LogoutSuccessHandlerImpl extends SimpleUrlLogoutSuccessHandler {

	private UserService userService;

    protected Logger logger = Logger.getLogger(this.getClass());

    
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
    
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		User user = (User) authentication.getPrincipal();
		logger.info("Logged out " + user);
		UserLogEntry userLogEntry = new UserLogEntry(user.getId(), "Log out");
    	userService.saveUserLogEntry(userLogEntry);
		super.handle(request, response, authentication);
	}

}
