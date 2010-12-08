package se.telescopesoftware.betpals.web;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;

public abstract class AbstractPalsController {

    protected Logger logger = Logger.getLogger(this.getClass());

	protected User getUser() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	return (User) authentication.getPrincipal();
	}
	
	protected Long getUserId() {
		return getUser().getId();
	}
	
	protected UserProfile getUserProfile() {
		return getUser().getUserProfile();
	}
}
