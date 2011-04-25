package se.telescopesoftware.betpals.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityType;
import se.telescopesoftware.betpals.domain.Alternative;
import se.telescopesoftware.betpals.domain.AlternativeType;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.CompetitionStatus;
import se.telescopesoftware.betpals.domain.Event;
import se.telescopesoftware.betpals.domain.Group;
import se.telescopesoftware.betpals.domain.InvitationHelper;
import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.ActivityService;
import se.telescopesoftware.betpals.services.CompetitionService;
import se.telescopesoftware.betpals.services.FacebookService;
import se.telescopesoftware.betpals.services.UserService;
import se.telescopesoftware.betpals.utils.ThumbnailUtil;

//TODO: Split to several controllers
@Controller
public class CompetitionController extends AbstractPalsController {

	private CompetitionService competitionService;
	private AccountService accountService;
	private ActivityService activityService;
	private FacebookService facebookService;
	private UserService userService;
	
    
    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Autowired
	public void setFacebookService(FacebookService facebookService) {
		this.facebookService = facebookService;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	

	@RequestMapping(value="/competitionview")	
	public String getView(@RequestParam(value="competitionId", required=false) Long competitionId, Model model) {
		Competition competition = null;
		if (competitionId != null) {
			competition = competitionService.getCompetitionById(competitionId);
		}
		model.addAttribute(competition != null ? competition : new Competition());
		return "createCompetitionView";
	}
	
	@RequestMapping(value="/competitionconfirmview")	
	public String getConfirmCompetitionView(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		model.addAttribute(competition);
		return "confirmCompetitionView";
	}
	
	@RequestMapping(value="/confirmcompetition")	
	public String confirmCompetition(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		competition.setStatus(CompetitionStatus.OPEN);
		competition = competitionService.saveCompetition(competition);
		model.addAttribute(competition);
		InvitationHelper invitationHelper = new InvitationHelper();
		invitationHelper.setCompetitionId(competitionId);
		model.addAttribute(invitationHelper);
    	model.addAttribute("groupList", userService.getUserGroups(getUserId()));
    	model.addAttribute("friendList", userService.getUserFriends(getUserId()));
		
		return "inviteToCompetitionView";
	}
	
	@RequestMapping(value="/competitionalternatives")	
	public String getCompetitionAlternativesView(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		Alternative alternative = new Alternative();
		alternative.setEventId(competition.getDefaultEvent().getId());
		alternative.setCompetitionId(competition.getId());
		model.addAttribute(alternative);
		model.addAttribute("alternativesList", competition.getDefaultEvent().getAlternatives());

		return "createAlternativeView";
	}
	
	@RequestMapping(value="/deletealternative")	
	public String deleteAlternative(@RequestParam("competitionId") Long competitionId, @RequestParam("alternativeId") Long alternativeId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		
		Set<Alternative> alternatives = competition.getDefaultEvent().getAlternatives();
		Set<Alternative> filteredAlternatives = new HashSet<Alternative>();
		for (Alternative alternative : alternatives ) {
			if (alternative.getId().compareTo(alternativeId) != 0) {
				filteredAlternatives.add(alternative);
			}
		}
		competition.getDefaultEvent().setAlternatives(filteredAlternatives);
		competition = competitionService.saveCompetition(competition);
		
		Alternative alternative = new Alternative();
		alternative.setEventId(competition.getDefaultEvent().getId());
		alternative.setCompetitionId(competition.getId());
		model.addAttribute(alternative);
		model.addAttribute("alternativesList", competition.getDefaultEvent().getAlternatives());
		
		return "createAlternativeView";
	}
	
	@RequestMapping(value="/savecompetition")	
	public String saveCompetition(@ModelAttribute("competition") Competition competition, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		logger.debug("Error found: " + result.getErrorCount());
    		return "createCompetitionView";
    	}
    	
    	competition.setOwnerId(getUserId());
    	Account account = accountService.getAccount(competition.getAccountId());
    	competition.setCurrency(account.getCurrency());
    	MultipartFile imageFile = competition.getImageFile();
    	boolean goToNextStep = competition.isGoToNextStep();
    	
    	Long competitionId = competition.getId();
    	
    	if (competitionId != null) {
    		Competition storedCompetition = competitionService.getCompetitionById(competition.getId());
    		competition.setEvents(storedCompetition.getEvents()); //TODO: Refactor this
    	}
    	
    	competition = competitionService.saveCompetition(competition);
    	saveImage(imageFile, competition.getId());
    	
    	if (competition.getEvents() == null || competition.getEvents().isEmpty()) {
    		logger.debug("Adding default event to competition");
    		Event event = new Event(competition.getName());
    		competition.addEvent(event);
        	competition = competitionService.saveCompetition(competition);
    	}

    	if (goToNextStep) {
    		Alternative alternative = new Alternative();
    		alternative.setEventId(competition.getDefaultEvent().getId());
    		alternative.setCompetitionId(competition.getId());
    		model.addAttribute(alternative);
    		model.addAttribute("alternativesList", competition.getDefaultEvent().getAlternatives());

    		return "createAlternativeView";
    	}
		return "userHomepageAction";
	}

	@RequestMapping(value="/invitetocompetition")	
	public String inviteToCompetition(@ModelAttribute("invitationHelper") InvitationHelper invitationHelper, BindingResult result, Model model) {
		if (result.hasErrors()) {
			logger.debug("Error found: " + result.getErrorCount());
			return "inviteToCompetitionView";
		}
		
		Competition competition = competitionService.getCompetitionById(invitationHelper.getCompetitionId());
		
    	Set<Long> friendsIdSet = new HashSet<Long>();
       	friendsIdSet.add(getUserId());
    	if (invitationHelper.isAllFriends()) {
    		friendsIdSet.addAll(getUserProfile().getFriendsIdSet());
    	} else {
    		friendsIdSet.addAll(invitationHelper.getFriendsIdSet());
    		Set<Long> groupIdSet = invitationHelper.getGroupIdSet();
    		for (Long groupId : groupIdSet) {
    			Group group = userService.getGroupById(groupId);
    			friendsIdSet.addAll(group.getMembersIdSet());
    		}
    	}
    	
    	competitionService.sendInvitationsToFriends(competition, friendsIdSet, getUserProfile());

    	//TODO: Implement community invitations
    	
    	if (invitationHelper.shouldInviteToFacebook()) {
    		facebookService.postCompetitionToUserWall(competition, getUserProfile());
    	}
    	
    	Activity activity = new Activity(getUserProfile(), ActivityType.MESSAGE);
    	activity.setMessage("Created new competition: " + competition.getName());
    	activityService.addActivity(activity);
		
		return "manageCompetitionsAction";
	}
	
	@RequestMapping(value="/savealternative")	
	public String saveAlternative(@Valid Alternative alternative, BindingResult result, Model model) {
		if (result.hasErrors()) {
			logger.debug("Error found: " + result.getErrorCount());
			return "createAlternativeView";
		}
		
		MultipartFile imageFile = alternative.getImageFile();
		Long eventId = alternative.getEventId();
		Long competitionId = alternative.getCompetitionId();
		
		alternative.setAlternativeType(AlternativeType.CUSTOM);
		alternative = competitionService.saveAlternative(alternative);
		saveAlternativeImage(imageFile, alternative.getId());

		Event event = competitionService.getEventById(eventId);
		event.addAlternative(alternative);
		event = competitionService.saveEvent(event);
		
		alternative = new Alternative();
		alternative.setEventId(eventId);
		alternative.setCompetitionId(competitionId);
		model.addAttribute(alternative);
		model.addAttribute("alternativesList", event.getAlternatives());
		return "createAlternativeView";
	}
	
    @RequestMapping(value="/join/{competitionHash}")
    protected String viewUserProfile(@PathVariable("competitionHash") String competitionHash, ModelMap model) {
    	if (Base64.isBase64(competitionHash.getBytes())) {
    		String decodedString = new String(Base64.decode(competitionHash.getBytes()));
    		String competitionIdString = decodedString.substring(decodedString.indexOf("/") + 1);
    		logger.info("Join competition link activated for competition: " + competitionIdString);
    		try {
    			Long competitionId = new Long(competitionIdString);
    			Competition competition = competitionService.getCompetitionById(competitionId);
    			model.addAttribute("competition", competition);
    		} catch (NumberFormatException ex) {
    			logger.error("Unparsable competition id in request: " + competitionIdString);
    		}
    	} else {
    		logger.error("Competition id in link is not encoded: " + competitionHash);
    	}
    	
        return "joinCompetitionView";
    }
	
	
    private void saveImage(MultipartFile imageFile, Long competitionId) {
        if (imageFile != null && !imageFile.isEmpty()) {
        	try {
	        	InputStream inputStream = imageFile.getInputStream();
	        	BufferedImage image = ImageIO.read(inputStream);
	        	BufferedImage thumbnailImage = ThumbnailUtil.getScaledInstance(image, 50, 50);
	
	        	String path = getAppRoot() + "images" + File.separator + "competitions";
	        	File outputFile = new File(path, competitionId + ".jpg");
	        	ImageIO.write(thumbnailImage, "jpg", outputFile);
	        	logger.debug("Writing competition picture to " + outputFile.getPath());

        	} catch(Exception ex) {
        		logger.error("Could not save image", ex);
        	}
        } else {
        	logger.debug("Using default picture for competition " + competitionId);
        }
    }

    private void saveAlternativeImage(MultipartFile imageFile, Long alternativeId) {
    	if (imageFile != null && !imageFile.isEmpty()) {
    		try {
    			InputStream inputStream = imageFile.getInputStream();
    			BufferedImage image = ImageIO.read(inputStream);
    			BufferedImage thumbnailImage = ThumbnailUtil.getScaledInstance(image, 50, 50);
    			
    			String path = getAppRoot() + "images" + File.separator + "competitions";
    			File outputFile = new File(path, "alt_" + alternativeId + ".jpg");
    			ImageIO.write(thumbnailImage, "jpg", outputFile);
    			logger.debug("Writing alternative picture to " + outputFile.getPath());
    			
    		} catch(Exception ex) {
    			logger.error("Could not save image", ex);
    		}
    	} else {
    		logger.debug("Using default picture for alternative " + alternativeId);
    	}
    }
    
}
