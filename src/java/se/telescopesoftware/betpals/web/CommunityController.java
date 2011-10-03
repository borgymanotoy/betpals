package se.telescopesoftware.betpals.web;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import se.telescopesoftware.betpals.domain.AccessType;
import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityType;
import se.telescopesoftware.betpals.domain.Community;
import se.telescopesoftware.betpals.domain.UserRequest;
import se.telescopesoftware.betpals.services.ActivityService;
import se.telescopesoftware.betpals.services.CompetitionService;

@Controller
public class CommunityController extends AbstractPalsController {

    private ActivityService activityService;
	private CompetitionService competitionService;

	
    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}

	
    @RequestMapping(value="/mycommunities")
    public String viewCommunities(Model model) {
    	model.addAttribute("tab", "communities");
    	return "friendsAndGroupsAction";
    }
	
    @RequestMapping(value="/deletecommunity")
    public String deleteCommunity(@RequestParam(value="communityId") Long communityId, Model model) {
    	getUserService().deleteCommunity(communityId, getUserId());
    	
    	model.addAttribute("tab", "communities");
    	return "friendsAndGroupsAction";
    }
    
    @RequestMapping(value="/editcommunity", method=RequestMethod.GET)
    protected String formBackingObject(@RequestParam(value="communityId", required=false) Long communityId, ModelMap model) {
    	if (communityId != null) {
    		Community community = getUserService().getCommunityById(communityId);
    		if (!community.checkOwnership(getUserId())) {
    			community = new Community(AccessType.PUBLIC);
    		}
    		model.addAttribute(community);
    	} else {
    		model.addAttribute(new Community(AccessType.PUBLIC));
    	}
        return "editCommunityView";
    }

    @RequestMapping(value="/editcommunity", method=RequestMethod.POST)
    protected String onSubmit(@Valid Community community, BindingResult result, Model model) {
    	
    	if (result.hasErrors()) {
    		logger.debug("Error found: " + result.getErrorCount());
    		return "editCommunityView";
    	}
    	
    	MultipartFile imageFile = community.getImageFile();

    	if (community.getId() == null) {
    		community.setCreated(new Date());
    		community.setOwnerId(getUserId());
    		community.addMember(getUserProfile());
    		logUserAction("Created " + community);
    	} else {
    		Community originalCommunity = getUserService().getCommunityById(community.getId());
    		originalCommunity.setName(community.getName());
    		originalCommunity.setDescription(community.getDescription());
    		community = originalCommunity;
    		logUserAction("Updated " + community);
    	}
        
    	community = getUserService().saveCommunity(community);
        saveImage(imageFile, IMAGE_FOLDER_COMMUNITIES, community.getId().toString());
        
    	model.addAttribute("tab", "communities");
        return "friendsAndGroupsAction";
    }
    
	@RequestMapping(value="/unjoincommunity")	
	public String unjoinCommunity(@RequestParam(value="communityId") Long communityId, Model model) {
		Community community = getUserService().getCommunityById(communityId);
		community.removeMember(getUserProfile());
		getUserService().saveCommunity(community);
		logUserAction("Unjoined " + community);
		
		model.addAttribute("tab", "communities");
        return "friendsAndGroupsAction";
	}
	
	@RequestMapping(value="/joincommunity")	
	public String joinCommunity(@RequestParam(value="communityId") Long communityId, Model model) {
		Community community = getUserService().getCommunityById(communityId);
		if (community.getAccessType() == AccessType.PUBLIC) {
			community.addMember(getUserProfile());
			getUserService().saveCommunity(community);
			logUserAction("Joined " + community);
		} else {
			getUserService().sendUserRequest(new UserRequest(getUserProfile(), community));
			logUserAction("Send request to join " + community);
		}
		
		model.addAttribute("tab", "communities");
		return "friendsAndGroupsAction";
	}
	
	@RequestMapping(value="/communities/{communityId}")	
	public String viewCommunity(@PathVariable Long communityId, @RequestParam(value="pageId", defaultValue="0", required=false) Integer pageId, Model model) {
		return getCommunityView(communityId, pageId, model);
	}
	
	@RequestMapping(value="/viewcommunity")	
	public String viewCommunityRedirect(@RequestParam(value="communityId") Long communityId, @RequestParam(value="pageId", defaultValue="0", required=false) Integer pageId, Model model) {
		return getCommunityView(communityId, pageId, model);
	}
	
	private String getCommunityView(Long communityId, Integer pageId, Model model) {
		Community community = getUserService().getCommunityById(communityId);
		model.addAttribute(community);
		
		if (community.checkMembership(getUserId())) {
			Collection<Activity> activities = activityService.getActivitiesForExtensionIdAndType(communityId, pageId, null, ActivityType.COMMUNITY);
			model.addAttribute(activities);
			model.addAttribute("numberOfPages", activityService.getActivitiesPageCountForExtensionIdAndType(communityId, ActivityType.COMMUNITY, null));
			model.addAttribute("invitationList", competitionService.getInvitationsForCommunity(communityId));
			return "viewCommunityMemberView";
		}
		return "viewCommunityView";
	}
	
	@RequestMapping(value="/communities/images/{communityId}")	
	public void getImage(@PathVariable String communityId, HttpServletResponse response) {
		sendJPEGImage(IMAGE_FOLDER_COMMUNITIES, communityId, response);
	}
	
	@RequestMapping(value="/savetempcommunityimage")	
	public void saveTempImage(@RequestParam("imageFile") MultipartFile imageFile, HttpServletResponse response) {
		String filename = "tmp" + getUserId();
		boolean success = saveImage(imageFile, IMAGE_FOLDER_COMMUNITIES, filename);
		if (success) {
			String message = "{\"success\":\"true\", \"filename\":\"" + filename + "\"}";
			response.setContentType("text/html");
			sendResponseStatusAndMessage(response, HttpServletResponse.SC_OK, message);
		} else {
			String message = "{\"success\":\"false\", \"filename\":\"" + filename + "\"}";
			response.setContentType("text/html");
			sendResponseStatusAndMessage(response, HttpServletResponse.SC_OK, message);
		}
	}

    
}
