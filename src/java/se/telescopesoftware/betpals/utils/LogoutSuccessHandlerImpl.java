package se.telescopesoftware.betpals.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import se.telescopesoftware.betpals.domain.User;

public class LogoutSuccessHandlerImpl extends SimpleUrlLogoutSuccessHandler {

    protected Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		User user = (User) authentication.getPrincipal();
		logger.info("Logged out " + user);
		super.handle(request, response, authentication);
	}

}
