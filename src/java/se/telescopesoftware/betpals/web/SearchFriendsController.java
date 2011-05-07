package se.telescopesoftware.betpals.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.UserRequestType;
import se.telescopesoftware.betpals.services.UserService;

@Controller
public class SearchFriendsController extends AbstractPalsController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value="/allfriends")
    public String get(@RequestParam(value="tab", required=false) String selectedTab, Model model) {
    	model.addAttribute("friendsList", getUserProfile().getFriends());
    	model.addAttribute("groupList", userService.getUserGroups(getUserId()));
    	model.addAttribute("communityList", userService.getUserCommunities(getUserId()));
    	model.addAttribute("userRequestFriendList", userService.getUserRequestsByUserByType(getUserId(), UserRequestType.FRIEND));
    	model.addAttribute("userRequestCommunityList", userService.getUserRequestsByUserByType(getUserId(), UserRequestType.COMMUNITY));

    	model.addAttribute("tab", selectedTab);
    	
    	return "friendsAndGroupsView";
    }
    
    @RequestMapping(value="/searchfriends")
    public String post(@RequestParam("query") String query, Model model) {
    	model.addAttribute("friendsList", userService.searchUserProfiles(query, getUserId()));
    	model.addAttribute("communityList", userService.searchCommunities(query));
    	return "friendsResultsView";
    }
    
}
