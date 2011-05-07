package se.telescopesoftware.betpals.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import se.telescopesoftware.betpals.domain.FacebookUser;
import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.services.FacebookService;
import se.telescopesoftware.betpals.services.UserService;

@Controller
public class FacebookLoginController extends AbstractPalsController {
	
	private UserService userService;
	private AuthenticationManager authenticationManager;
	private FacebookService facebookService;
	

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setFacebookService(FacebookService facebookService) {
		this.facebookService = facebookService;
	}
	
	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	

	@RequestMapping(value="/facebooklogin")
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String code = ServletRequestUtils.getStringParameter(request, "code");
		
		if (code == null || code.length() == 0) {
			logger.debug("redirecting to facebook..");
			return new ModelAndView(new RedirectView(facebookService.getAuthorizeURL()));
		} else {
			String accessToken = facebookService.getAccessToken(code);
			
			if (accessToken != null) {
				FacebookUser facebookUser = facebookService.getUserDetails(accessToken);
				if (facebookUser != null) {
					User user = null;
					try {
						user = userService.getUserByUsername(facebookUser.getMybetpalsUsername());
					} catch(UsernameNotFoundException ex) {
						logger.info("New Facebook user.");
						Long userId = userService.registerFacebookUser(facebookUser);
						user = userService.getUserByUserId(userId);
					}
					
					logger.info("Login from facebook for user: " + user.toString());
					Authentication auth = new UsernamePasswordAuthenticationToken(facebookUser.getMybetpalsUsername(), facebookUser.getMybetpalsPassword());
					SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(auth));
					
					getUserProfile().setFacebookAccessToken(accessToken);
					getUserProfile().setFacebookUser(facebookUser);
				}
			}
		}

		return new ModelAndView("userHomepageAction");
	}
	
	
}
