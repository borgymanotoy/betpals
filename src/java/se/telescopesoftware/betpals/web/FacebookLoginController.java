package se.telescopesoftware.betpals.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import se.telescopesoftware.betpals.domain.FacebookUser;
import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.FacebookService;

@Controller
public class FacebookLoginController extends AbstractPalsController {
	
	private AuthenticationManager authenticationManager;
	private FacebookService facebookService;
	private AccountService accountService;
	

	@Autowired
	public void setFacebookService(FacebookService facebookService) {
		this.facebookService = facebookService;
	}
	
	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	

	@RequestMapping(value="/facebooklogin")
	protected ModelAndView processLogin(@RequestParam(value="code", required = false) String code, HttpServletRequest request, Locale locale) {

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
						user = getUserService().getUserByUsername(facebookUser.getMybetpalsUsername());
					} catch(UsernameNotFoundException ex) {
						logger.info("New Facebook user.");
					}

					if (user == null) {
						Long userId = getUserService().registerFacebookUser(facebookUser, locale);
						user = getUserService().getUserByUserId(userId);
				    	accountService.createDefaultAccountForUser(userId);
					}
					
					logger.info("Login from facebook for user: " + user.toString());
					logUserAction(user.getId(), "Login from Facebook");
					Authentication auth = new UsernamePasswordAuthenticationToken(facebookUser.getMybetpalsUsername(), facebookUser.getMybetpalsPassword());
					SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(auth));
					
					getUserProfile().setFacebookAccessToken(accessToken);
					getUserProfile().setFacebookUser(facebookUser);
					UserProfile userProfile = getUserProfile();
					userProfile.registerVisit();
					getUserService().updateUserProfile(userProfile);

				}
			}
		}

        String redirectUrl = getRedirectUrl(request);
        return redirectUrl != null ? new ModelAndView(new RedirectView(redirectUrl)) : new ModelAndView("userHomepageAction");
	}
	
	
}
