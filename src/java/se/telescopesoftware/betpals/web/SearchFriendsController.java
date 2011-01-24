package se.telescopesoftware.betpals.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.services.UserService;

@Controller
public class SearchFriendsController extends AbstractPalsController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value="/allfriends", method = RequestMethod.GET)
    public String get(Model model) {
    	model.addAttribute("friendsList", userService.getUserFriends(getUserId()));
    	model.addAttribute("userRequestList", userService.getUserRequestByUser(getUserId()));

    	return "friendsAndGroupsView";
    }
    
    @RequestMapping(value="/searchfriends", method = RequestMethod.POST)
    public String post(@RequestParam("query") String query, Model model) {
    	model.addAttribute("friendsList", userService.searchUserProfiles(query, getUserId()));
    	return "friendsResultsView";
    }
    
}
