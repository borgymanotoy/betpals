package se.telescopesoftware.betpals.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Community;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.domain.UserRequest;
import se.telescopesoftware.betpals.domain.UserRequestType;
import se.telescopesoftware.betpals.services.UserService;

@Controller
public class EditFriendsController extends AbstractPalsController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value="/invitefriend")
    public String inviteFriend(@RequestParam("friendId") Long friendId) {
    	UserProfile friendProfile = userService.getUserProfileByUserId(friendId);
    	userService.sendUserRequest(new UserRequest(getUserProfile(), friendProfile));
    	return "friendsAndGroupsAction";
    }
    
    /**
     * Accept request and add friend.
     */
    @RequestMapping(value="/acceptrequest")
    public String acceptRequest(@RequestParam("requestId") Long requestId) {
    	UserRequest userRequest = userService.getUserRequestById(requestId);
    	UserProfile friendProfile = userService.getUserProfileByUserId(userRequest.getOwnerId());

    	if (userRequest.getRequestType() == UserRequestType.COMMUNITY) {
    		Community community = userService.getCommunityById(userRequest.getExtensionId());
    		community.addMember(friendProfile);
    		userService.saveCommunity(community);
    	} else {
    		friendProfile.addFriend(getUserProfile());
    		userService.updateUserProfile(friendProfile);
    	}
    	
    	userService.deleteUserRequest(requestId);
    	
    	return "userRequestsListAction";
    }

    @RequestMapping(value="/rejectrequest")
    public String rejectRequest(@RequestParam("requestId") Long requestId, Model model) {
    	userService.deleteUserRequest(requestId);
    	return "userRequestsListAction";
    }
    
    @RequestMapping(value="/myrequests")
    public String viewUserRequests(Model model) {
    	model.addAttribute("userRequestFriendList", userService.getUserRequestsForUserByType(getUserId(), UserRequestType.FRIEND));
    	model.addAttribute("userRequestCommunityList", userService.getUserRequestsForUserByType(getUserId(), UserRequestType.COMMUNITY));
    	return "userRequestsListView";
    }

}
