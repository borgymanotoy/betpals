package se.telescopesoftware.betpals.web;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Group;

@Controller
public class GroupsController extends AbstractPalsController {


    @RequestMapping(value="/mygroups")
    public String viewGroups(Model model) {
    	model.addAttribute("tab", "groups");
    	return "friendsAndGroupsAction";
    }
	
    @RequestMapping(value="/deletegroup")
    public String deleteGroup(@RequestParam(value="groupId") Long groupId, Model model) {
		Group group = getUserService().getGroupById(groupId);
		logUserAction("Delete " + group);
		getUserService().deleteGroup(groupId, getUserId());
    	model.addAttribute("tab", "groups");
    	return "friendsAndGroupsAction";
    }
    
    @RequestMapping(value="/editgroup", method=RequestMethod.GET)
    protected String formBackingObject(@RequestParam(value="groupId", required=false) Long groupId, ModelMap model) {
    	if (groupId != null) {
    		Group group = getUserService().getGroupById(groupId);
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
    		logUserAction("Create " + group);
    	}
    	getUserService().saveGroup(group);
		logUserAction("Update " + group);
        
    	model.addAttribute("tab", "groups");
        return "friendsAndGroupsAction";
    }
    
}
