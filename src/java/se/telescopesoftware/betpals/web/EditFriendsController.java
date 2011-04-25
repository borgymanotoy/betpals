package se.telescopesoftware.betpals.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.domain.UserRequest;
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
    	UserRequest request = new UserRequest();
    	request.setInviteeId(friendId);
    	request.setOwnerId(getUserId());
    	request.setOwnerName(getUserProfile().getFullName());

    	UserProfile friendProfile = userService.getUserProfileByUserId(friendId);
    	request.setInviteeName(friendProfile.getFullName());
    	
    	userService.sendUserRequest(request);
    	return "friendsAndGroupsAction";
    }
    
    /**
     * Accept request and add friend.
     */
    @RequestMapping(value="/acceptrequest")
    public String acceptRequest(@RequestParam("friendId") Long friendId, @RequestParam("requestId") Long requestId) {
    	
//    	UserProfile userProfile = getUserProfile();
//    	userProfile.addFriend(friendId);
    	
//    	userService.updateUserProfile(userProfile);

    	UserProfile friendProfile = userService.getUserProfileByUserId(friendId);
    	friendProfile.addFriend(getUserId());
    	userService.updateUserProfile(friendProfile);
    	userService.deleteUserRequest(requestId);
    	
    	return "friendsAndGroupsAction";
    }

    @RequestMapping(value="/rejectrequest")
    public String rejectRequest(@RequestParam("requestId") Long requestId, Model model) {
    	userService.deleteUserRequest(requestId);
    	model.addAttribute("userRequestList", userService.getUserRequestForUser(getUserId()));
    	return "userRequestsListView";
    }
    
    @RequestMapping(value="/myrequests")
    public String viewUserRequests(Model model) {
    	model.addAttribute("userRequestList", userService.getUserRequestForUser(getUserId()));
    	return "userRequestsListView";
    }

}
