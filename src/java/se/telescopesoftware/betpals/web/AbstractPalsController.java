package se.telescopesoftware.betpals.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;

/**
 * Some common functionality and helper methods for mybetpals controllers.
 * Extend this class when creating new controller.
 *
 */
public abstract class AbstractPalsController {

    private String appRoot;

    protected Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public void setAppRoot(String appRoot) {
    	this.appRoot = appRoot;
    }
    
    /**
     * Return system path to application root folder.
     * @return Absolute path to webapp root folder
     */
	protected String getAppRoot() {
		return appRoot;
	}

	/**
	 * Get logged on user.
	 * @return current authenticated User.
	 */
	protected User getUser() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	return (User) authentication.getPrincipal();
	}
	
	/**
	 * Get logged on user id.
	 * @return current authenticated User id.
	 */
	protected Long getUserId() {
		return getUser().getId();
	}
	
	/**
	 * Get UserProfile for logged on user.
	 * @return UserProfile of current authenticated User.
	 */
	protected UserProfile getUserProfile() {
		return getUser().getUserProfile();
	}
}
