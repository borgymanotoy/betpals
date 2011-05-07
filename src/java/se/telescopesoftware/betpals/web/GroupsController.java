package se.telescopesoftware.betpals.web;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Group;
import se.telescopesoftware.betpals.services.UserService;

@Controller
public class GroupsController extends AbstractPalsController {

    private UserService userService;

	
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(value="/mygroups")
    public String viewGroups(Model model) {
    	model.addAttribute("tab", "groups");
    	return "friendsAndGroupsAction";
    }
	
    @RequestMapping(value="/deletegroup")
    public String deleteGroup(@RequestParam(value="groupId") Long groupId, Model model) {
		userService.deleteGroup(groupId, getUserId());
    	
    	model.addAttribute("tab", "groups");
    	return "friendsAndGroupsAction";
    }
    
    @RequestMapping(value="/editgroup", method=RequestMethod.GET)
    protected String formBackingObject(@RequestParam(value="groupId", required=false) Long groupId, ModelMap model) {
    	if (groupId != null) {
    		Group group = userService.getGroupById(groupId);
    		if (!group.checkOwnership(getUserId())) {
    			group = new Group();
    		}
    		model.addAttribute(group);
    	} else {
    		model.addAttribute(new Group());
    	}
    	model.addAttribute("friendsList", getUserProfile().getFriends());
        return "editGroupView";
    }

    @RequestMapping(value="/editgroup", method=RequestMethod.POST)
    protected String onSubmit(@Valid Group group, BindingResult result, Model model) {
    	
    	if (result.hasErrors()) {
    		logger.debug("Error found: " + result.getErrorCount());
    		return "editGroupView";
    	}
    	if (group.getId() == null) {
    		group.setCreated(new Date());
    		group.setOwnerId(getUserId());
    	}
        userService.saveGroup(group);
        
    	model.addAttribute("tab", "groups");
        return "friendsAndGroupsAction";
    }
    
}
