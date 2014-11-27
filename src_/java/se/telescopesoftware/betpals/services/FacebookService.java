package se.telescopesoftware.betpals.services;

import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.FacebookUser;
import se.telescopesoftware.betpals.domain.UserProfile;

/**
 * All facebook related functionality is accessible through this service.
 *
 */
public interface FacebookService {

	/**
	 * Post specified message to user's facebook wall, if user has facebook account and logged in.
	 * 
	 * @param message
	 * @param userProfile
	 * @param linkUrl link back to mybetpals page
	 */
	void postMessageToUserWall(String message, UserProfile userProfile, String linkUrl);
	
	/**
	 * Post information about competition to user's facebook wall.
	 * 
	 * @param competition
	 * @param userProfile
	 */
	void postCompetitionToUserWall(Competition competition, UserProfile userProfile);
	
	/**
	 * Populates facebook authorize url with application id and other required data.
	 * 
	 * @return populated facebook authorize url
	 */
	String getAuthorizeURL();
	
	/**
	 * Return facebook access token. From facebook docs:
	 * If your app is successfully authenticated and the authorization code from the user is valid, 
	 * the authorization server will return the access token.
	 * In addition to the access token (the access_token parameter), 
	 * the response contains the number of seconds until the token expires (the expires parameter). 
	 * Once the token expires, you will need to re-run the steps above to generate a new code and access_token, 
	 * although if the user has already authorized your app, they will not be prompted to do so again.
	 * 
	 * If there is an issue authenticating your app, the authorization server will issue an HTTP 400 
	 * and return the error in the body of the response.
	 * 
	 * @param code - code returned by facebook after authorization
	 * @return access token string or null
	 */
	String getAccessToken(String code);
	
	
	FacebookUser getUserDetails(String accessToken);
}
