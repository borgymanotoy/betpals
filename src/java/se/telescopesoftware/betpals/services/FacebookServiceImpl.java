package se.telescopesoftware.betpals.services;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.FacebookUser;
import se.telescopesoftware.betpals.domain.UserProfile;

public class FacebookServiceImpl implements FacebookService {

	private String facebookClientId;
	private String facebookClientSecret;
	private String facebookReturnUrl;
	private String facebookAuthorizeUrl = "https://graph.facebook.com/oauth/authorize";
	private String facebookAccessTokenUrl = "https://graph.facebook.com/oauth/access_token";
	private String facebookUserInfoUrl = "https://graph.facebook.com/me";
	private String facebookApiUrl = "https://graph.facebook.com/";
	private String competitionUrl = "https://www.mybetpals.com/join/";

    private static Logger logger = Logger.getLogger(FacebookServiceImpl.class);

	public void setFacebookClientId(String clientId) {
		this.facebookClientId = clientId;
	}
	
	public void setFacebookClientSecret(String clientSecret) {
		this.facebookClientSecret = clientSecret;
	}
	
	public void setFacebookReturnUrl(String facebookReturnUrl) {
		this.facebookReturnUrl = facebookReturnUrl;
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

	public void setFacebookApiUrl(String facebookApiUrl) {
		this.facebookApiUrl = facebookApiUrl;
	}

	public void setCompetitionUrl(String competitionUrl) {
		this.competitionUrl = competitionUrl;
	}

	public void postMessageToUserWall(String message, UserProfile userProfile, String linkUrl) {
		FacebookUser facebookUser = userProfile.getFacebookUser();
		logger.debug("Request to post message to facebook: " + message);
		if (facebookUser != null) {
			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.postForObject("{url}?access_token={token}&message={message}&link={link}",
					null,
					String.class,
					facebookApiUrl + facebookUser.getId() + "/feed",
					userProfile.getFacebookAccessToken(),
					message,
					linkUrl);
			logger.debug("Post to facebook result: " + result);
		}
	}

	public String getAuthorizeURL() {
		StringBuffer sb = new StringBuffer(facebookAuthorizeUrl);
		sb.append("?client_id=");
		sb.append(facebookClientId);
		sb.append("&redirect_uri=");
		sb.append(facebookReturnUrl);
		sb.append("&scope=email,read_stream,publish_stream");
		return sb.toString();
	}

	public String getAccessToken(String code) {
		String accessToken = null;
		RestTemplate restTemplate = new RestTemplate();
		String accessTokenString = 
			restTemplate.getForObject("{url}?client_id={clientId}&redirect_uri={redirect}&client_secret={secret}&code={code}", 
					String.class, 
					facebookAccessTokenUrl, 
					facebookClientId, 
					facebookReturnUrl, 
					facebookClientSecret, 
					code);
		logger.debug("access token string: " + accessTokenString);
		if (accessTokenString != null) {
			accessToken = accessTokenString.substring(accessTokenString.indexOf('=') + 1, accessTokenString.indexOf('&'));
			logger.debug("access token: " + accessToken);
		}
		//TODO: handle token expiration
		
		return accessToken;
	}

	public FacebookUser getUserDetails(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();
		String userDetailsJSON = restTemplate.getForObject("{url}?access_token={token}", String.class, facebookUserInfoUrl, accessToken);
		logger.debug("Facebook user details: " + userDetailsJSON);
		FacebookUser facebookUser = new FacebookUser(userDetailsJSON);
		
		return facebookUser;
	}

	public void postCompetitionToUserWall(Competition competition, UserProfile userProfile) {
		String message = userProfile.getFullName() + " just created the competition " + competition.getName() + " at Mybetpals.";
		postMessageToUserWall(message, userProfile, competitionUrl + competition.getEncodedLink());
	}

}
