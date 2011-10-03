package se.telescopesoftware.betpals.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Community;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.domain.UserRequest;
import se.telescopesoftware.betpals.domain.UserRequestType;

@Controller
public class EditFriendsController extends AbstractPalsController {


    @RequestMapping(value="/invitefriend")
    public String inviteFriend(@RequestParam("friendId") Long friendId) {
    	if (friendId.compareTo(getUserId()) != 0) {
	    	UserProfile friendProfile = getUserService().getUserProfileByUserId(friendId);
	    	UserRequest userRequest = new UserRequest(getUserProfile(), friendProfile);
	    	getUserService().sendUserRequest(userRequest);
	    	logUserAction("Send " + userRequest);
    	}
    	return "friendsAndGroupsAction";
    }
    
    /**
     * Accept request and add friend.
     */
    @RequestMapping(value="/acceptrequest")
    public String acceptRequest(@RequestParam("requestId") Long requestId) {
    	UserRequest userRequest = getUserService().getUserRequestById(requestId);
    	UserProfile friendProfile = getUserService().getUserProfileByUserId(userRequest.getOwnerId());

    	if (userRequest.getRequestType() == UserRequestType.COMMUNITY) {
    		Community community = getUserService().getCommunityById(userRequest.getExtensionId());
    		community.addMember(friendProfile);
    		getUserService().saveCommunity(community);
    	} else {
    		UserProfile currentUserProfile = getUserProfile();
    		friendProfile.addFriend(currentUserProfile);
    		currentUserProfile.addFriend(friendProfile);
    		
    		getUserService().updateUserProfile(friendProfile);
    		getUserService().updateUserProfile(currentUserProfile);
    	}
    	
    	logUserAction("Accept " + userRequest);
    	getUserService().deleteUserRequest(requestId);
    	
    	return "userRequestsListAction";
    }

    @RequestMapping(value="/rejectrequest")
    public String rejectRequest(@RequestParam("requestId") Long requestId, Model model) {
    	UserRequest userRequest = getUserService().getUserRequestById(requestId);
    	logUserAction("Reject " + userRequest);
    	getUserService().deleteUserRequest(requestId);
    	return "userRequestsListAction";
    }
    
    @RequestMapping(value="/myrequests")
    public String viewUserRequests(HttpSession session, Model model) {
    	model.addAttribute("userRequestFriendList", getUserService().getUserRequestsForUserByType(getUserId(), UserRequestType.FRIEND));
    	model.addAttribute("userRequestCommunityList", getUserService().getUserRequestsForUserByType(getUserId(), UserRequestType.COMMUNITY));
    	session.setAttribute("myRequestsCount", getUserService().getUserRequestForUserCount(getUserId()));
    	session.setAttribute("friendsSideList", getUserProfile().getLastLoggedInFriends());    	

    	return "userRequestsListView";
    }

    @RequestMapping(value="/deletefriend")
    public String deleteFriend(@RequestParam("friendId") Long friendId, Model model) {
    	UserProfile friendProfile = getUserService().getUserProfileByUserId(friendId);
    	UserProfile userProfile = getUserProfile();
    	userProfile.removeFriend(friendProfile);
    	getUserService().updateUserProfile(userProfile);
    	logUserAction("Removing friend " + friendProfile);
    	return "friendsAndGroupsAction";
    }
    
    
}
