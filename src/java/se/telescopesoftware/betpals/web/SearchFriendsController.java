package se.telescopesoftware.betpals.web;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.domain.UserRequestType;

@Controller
public class SearchFriendsController extends AbstractPalsController {


    @RequestMapping(value="/allfriends")
    public String get(@RequestParam(value="tab", required=false) String selectedTab, Model model, HttpSession session) {
    	model.addAttribute("friendsList", getUserProfile().getFriends());
    	model.addAttribute("groupList", getUserService().getUserGroups(getUserId()));
    	model.addAttribute("communityList", getUserService().getUserCommunities(getUserId()));
    	model.addAttribute("userRequestFriendList", getUserService().getUserRequestsByUserByType(getUserId(), UserRequestType.FRIEND));
    	model.addAttribute("userRequestCommunityList", getUserService().getUserRequestsByUserByType(getUserId(), UserRequestType.COMMUNITY));

    	model.addAttribute("tab", selectedTab);
    	session.setAttribute("friendsSideList", getUserProfile().getLastLoggedInFriends());    	
    	
    	return "friendsAndGroupsView";
    }
    
    @RequestMapping(value="/searchfriends")
    public String post(@RequestParam("query") String query, Model model) {
    	Collection<UserProfile> friendList = getUserService().searchUserProfiles(query, getUserId());
    	markFriends(friendList);
    	model.addAttribute("friendsList", friendList);
    	model.addAttribute("communityList", getUserService().searchCommunities(query));
    	return "friendsResultsView";
    }
    
    private void markFriends(Collection<UserProfile> friendList) {
    	
    	for(UserProfile friendProfile : friendList) {
    		if (getUserProfile().isFriendWithUser(friendProfile)) {
    			friendProfile.setFriendWithCurrentUser(true);
    		}
    	}
    	
    }
    
}
