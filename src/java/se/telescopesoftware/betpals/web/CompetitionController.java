package se.telescopesoftware.betpals.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityType;
import se.telescopesoftware.betpals.domain.Alternative;
import se.telescopesoftware.betpals.domain.AlternativeType;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.Event;
import se.telescopesoftware.betpals.domain.InvitationHelper;
import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.ActivityService;
import se.telescopesoftware.betpals.services.CompetitionService;
import se.telescopesoftware.betpals.utils.ThumbnailUtil;

//TODO: Split to several controllers
@Controller
public class CompetitionController extends AbstractPalsController {

	private CompetitionService competitionService;
	private AccountService accountService;
	private ActivityService activityService;
    private String appRoot;

    private static Logger logger = Logger.getLogger(CompetitionQuickController.class);

    
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
    public void setAppRoot(String appRoot) {
    	this.appRoot = appRoot;
    }

	@RequestMapping(value="/competitionview", method = {RequestMethod.POST, RequestMethod.GET})	
	public String getView(@RequestParam(value="competitionId", required=false) Long competitionId, Model model) {
		Competition competition = null;
		if (competitionId != null) {
			competition = competitionService.getCompetitionById(competitionId);
		}
		model.addAttribute(competition != null ? competition : new Competition());
		return "createCompetitionView";
	}
	
	@RequestMapping(value="/competitionconfirmview", method = {RequestMethod.POST, RequestMethod.GET})	
	public String getConfirmCompetitionView(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		model.addAttribute(competition);
		return "confirmCompetitionView";
	}
	
	@RequestMapping(value="/confirmcompetition", method = {RequestMethod.POST, RequestMethod.GET})	
	public String confirmCompetition(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		model.addAttribute(competition);
		InvitationHelper invitationHelper = new InvitationHelper();
		invitationHelper.setCompetitionId(competitionId);
		model.addAttribute(invitationHelper);
		return "inviteToCompetitionView";
	}
	
	@RequestMapping(value="/competitionalternatives", method = {RequestMethod.POST, RequestMethod.GET})	
	public String getCompetitionAlternativesView(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		Alternative alternative = new Alternative();
		alternative.setEventId(competition.getDefaultEvent().getId());
		alternative.setCompetitionId(competition.getId());
		model.addAttribute(alternative);
		model.addAttribute("alternativesList", competition.getDefaultEvent().getAlternatives());

		return "createAlternativeView";
	}
	
	@RequestMapping(value="/deletealternative", method = {RequestMethod.POST, RequestMethod.GET})	
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
		competition = competitionService.addCompetition(competition);
		
		Alternative alternative = new Alternative();
		alternative.setEventId(competition.getDefaultEvent().getId());
		alternative.setCompetitionId(competition.getId());
		model.addAttribute(alternative);
		model.addAttribute("alternativesList", competition.getDefaultEvent().getAlternatives());
		
		return "createAlternativeView";
	}
	
	@RequestMapping(value="/savecompetition", method = {RequestMethod.POST, RequestMethod.GET})	
	public String processSubmit(@ModelAttribute("competition") Competition competition, BindingResult result, Model model) {
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
    	
    	competition = competitionService.addCompetition(competition);
    	saveImage(imageFile, competition.getId());
    	
    	if (competition.getEvents() == null || competition.getEvents().isEmpty()) {
    		logger.debug("Adding default event to competition");
    		Event event = new Event(competition.getName());
    		competition.addEvent(event);
        	competition = competitionService.addCompetition(competition);
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

	@RequestMapping(value="/invitetocompetition", method = {RequestMethod.POST, RequestMethod.GET})	
	public String inviteToCompetition(@ModelAttribute("invitationHelper") InvitationHelper invitationHelper, BindingResult result, Model model) {
		if (result.hasErrors()) {
			logger.debug("Error found: " + result.getErrorCount());
			return "inviteToCompetitionView";
		}
		
		Competition competition = competitionService.getCompetitionById(invitationHelper.getCompetitionId());
		
    	if (invitationHelper.isAllFriends()) {
    		competitionService.sendInvitationsToFriends(competition, getUserProfile().getFriendsIdSet(), getUserProfile());
    	} else {
    		competitionService.sendInvitationsToFriends(competition, invitationHelper.getFriendsIdSet(), getUserProfile());
    	}
    	
    	//TODO: Implement group invitations
    	//TODO: Implement community invitations
    	//TODO: Implement facebook publishing
    	
    	Activity activity = new Activity();
    	activity.setCreated(new Date());
    	activity.setOwnerId(getUserId());
    	activity.setOwnerName(getUserProfile().getFullName());
    	activity.setActivityType(ActivityType.MESSAGE);
    	activity.setMessage("Created new competition: " + competition.getName());
    	
    	activityService.addActivity(activity);
		
		return "manageCompetitionsAction";
	}
	
	@RequestMapping(value="/savealternative", method = RequestMethod.POST)	
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
	
    private void saveImage(MultipartFile imageFile, Long competitionId) {
        if (imageFile != null && !imageFile.isEmpty()) {
        	try {
	        	InputStream inputStream = imageFile.getInputStream();
	        	BufferedImage image = ImageIO.read(inputStream);
	        	BufferedImage thumbnailImage = ThumbnailUtil.getScaledInstance(image, 50, 50);
	
	        	String path = appRoot + "images" + File.separator + "competitions";
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
    			
    			String path = appRoot + "images" + File.separator + "competitions";
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
