package se.telescopesoftware.betpals.utils;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.bind.ServletRequestUtils;

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserLogEntry;
import se.telescopesoftware.betpals.services.UserService;


public class SwitchUserSuccessAuthenticationHandlerImpl extends SimpleUrlAuthenticationSuccessHandler {

	private UserService userService;

    protected Logger logger = Logger.getLogger(this.getClass());
	
	
    public void setUserService(UserService userService) {
		this.userService = userService;
	}
    
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		if (isSwitchingToUser(authentication)) {
			User user = (User) authentication.getPrincipal();
			logger.info("Switching in to " + user);
			UserLogEntry userLogEntry = new UserLogEntry(user.getId(), "Switched in by admin");
	    	userService.saveUserLogEntry(userLogEntry);
		}
		
		String targetUrl = ServletRequestUtils.getStringParameter(request, "targetUrl");
		String returnUrl = ServletRequestUtils.getStringParameter(request, "returnUrl");
		Long exitUserId = ServletRequestUtils.getLongParameter(request, "exitUserId");
		if (exitUserId != null) {
			User exitUser = userService.getUserByUserId(exitUserId);
			logger.info("Switching out from " + exitUser);
			UserLogEntry userLogEntry = new UserLogEntry(exitUserId, "Switched out by admin");
	    	userService.saveUserLogEntry(userLogEntry);
		}
		
		if (targetUrl != null) {
			logger.debug("Default target url is " + getDefaultTargetUrl());
			logger.debug("Setting target url to " + targetUrl);
			setTargetUrlParameter("targetUrl");
		}
		
		HttpSession session = request.getSession();
		if (returnUrl != null) {
			session.setAttribute("switchUserReturnUrl", returnUrl);
		} else {
			session.removeAttribute("switchUserReturnUrl");
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

	private boolean isSwitchingToUser(Authentication authentication) {
		Collection<GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().equalsIgnoreCase("ROLE_PREVIOUS_ADMINISTRATOR")) {
				return true;
			}
		}
		return false;
	}
	
}
