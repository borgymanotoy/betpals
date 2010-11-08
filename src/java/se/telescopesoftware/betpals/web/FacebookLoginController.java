package se.telescopesoftware.betpals.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.UserService;

public class FacebookLoginController extends ParameterizableViewController {
	
	private UserService userService;
	private AuthenticationManager authenticationManager;
	
	private String clientId;
	private String clientSecret;
	private String returnUrl;
	private String facebookAuthorizeUrl;
	private String facebookAccessTokenUrl;
	private String facebookUserInfoUrl;

	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public void setFacebookAuthorizeUrl(String facebookAuthorizeUrl) {
		this.facebookAuthorizeUrl = facebookAuthorizeUrl;
	}

	public void setFacebookAccessTokenUrl(String facebookAccessTokenUrl) {
		this.facebookAccessTokenUrl = facebookAccessTokenUrl;
	}

	public void setFacebookUserInfoUrl(String facebookUserInfoUrl) {
		this.facebookUserInfoUrl = facebookUserInfoUrl;
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String code = ServletRequestUtils.getStringParameter(request, "code");
		
		if (code == null || code.length() == 0) {
			logger.debug("redirecting to facebook..");
			String redirectUrl = facebookAuthorizeUrl + "?client_id=" + clientId + "&redirect_uri=" + returnUrl;
			RedirectView rView = new RedirectView(redirectUrl);
			return new ModelAndView(rView);
		} else {
			RestTemplate restTemplate = new RestTemplate();
			String accessTokenString = restTemplate.getForObject("{url}?client_id={clientId}&redirect_uri={redirect}&client_secret={secret}&code={code}", String.class, facebookAccessTokenUrl, clientId, returnUrl, clientSecret, code);
			logger.debug("access token string: " + accessTokenString);
			
			if (accessTokenString != null) {
				String accessToken = accessTokenString.substring(accessTokenString.indexOf('=') + 1, accessTokenString.indexOf('&'));
				logger.debug("access token: " + accessToken);
				String userDetails = restTemplate.getForObject("{url}?access_token={token}", String.class, facebookUserInfoUrl, accessToken);
				logger.debug("user details: " + userDetails);
				
				JSONObject jObject = (JSONObject) JSONSerializer.toJSON(userDetails);
				String firstName = jObject.getString("first_name");
				String lastName = jObject.getString("last_name");
				String id = jObject.getString("id");
				
				logger.debug("User id: " + id);
				logger.debug("User first name: " + firstName);
				logger.debug("User last name: " + lastName);
				
				String username = "fb_" + id;
				String password = "fb_" + id;
				
				User user = null;
				try {
					user = userService.getUserByUsername(username);
				} catch(UsernameNotFoundException ex) {
					logger.info("New Facebook user.");
				}
				
				if (user == null) {
					user = new User(username);
					user.encodeAndSetPassword(password);
					Long userId = userService.registerUser(user);
					UserProfile userProfile = new UserProfile();
					userProfile.setUserId(userId);
					userProfile.setName(firstName);
					userProfile.setSurname(lastName);
					userProfile.setRegistrationDate(new Date());
					userService.updateUserProfile(userProfile);
				}
				Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
				SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(auth));
			}
		}

		return new ModelAndView(getViewName());
	}
	
	
}
