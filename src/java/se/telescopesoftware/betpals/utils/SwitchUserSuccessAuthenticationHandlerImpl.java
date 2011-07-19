package se.telescopesoftware.betpals.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.bind.ServletRequestUtils;


public class SwitchUserSuccessAuthenticationHandlerImpl extends SimpleUrlAuthenticationSuccessHandler {

    protected Logger logger = Logger.getLogger(this.getClass());
	
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		String targetUrl = ServletRequestUtils.getStringParameter(request, "targetUrl");
		String returnUrl = ServletRequestUtils.getStringParameter(request, "returnUrl");
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

}
