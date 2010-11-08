package se.telescopesoftware.betpals.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.UserService;

@Controller
public class SearchFriendsController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value="/allfriends", method = RequestMethod.GET)
    public String get(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	User user = (User) authentication.getPrincipal();
    	
    	Collection<UserProfile> friends = userService.getUserFriends(user.getId());
    	model.addAttribute("friendsList", friends);
    	return "friendsAndGroupsView";
    }
    
    @RequestMapping(value="/searchfriends", method = RequestMethod.POST)
    public String post(@RequestParam("query") String query, Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	User user = (User) authentication.getPrincipal();

    	model.addAttribute("friendsList", userService.searchUserProfiles(query, user.getId()));
    	return "friendsResultsView";
    }
    
}
