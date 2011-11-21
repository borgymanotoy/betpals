package se.telescopesoftware.betpals.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.ActivityService;
import se.telescopesoftware.betpals.services.CompetitionService;


@Controller
public class HomeController extends AbstractPalsController {

    private ActivityService activityService;
    private AccountService accountService;
    private CompetitionService competitionService;


    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }
    
    @Autowired
    public void setAccountService(AccountService accountService) {
    	this.accountService = accountService;
    }
    
    @Autowired
    public void setCompetitionService(CompetitionService competitionService) {
    	this.competitionService = competitionService;
    }

	@RequestMapping("/home")
	public String get(@RequestParam(value="pageId", defaultValue="0", required=false) Integer pageId, Model model, HttpSession session) {
    	Collection<Activity> activities = activityService.getActivitiesForUserProfile(getUserProfile(), pageId, null);
    	Collection<Account> accounts = accountService.getUserAccounts(getUserId());
    	
    	session.setAttribute("friendsSideList", getUserProfile().getLastLoggedInFriends());    	
    	session.setAttribute("user", getUserProfile());
    	session.setAttribute("accounts", accounts);
    	session.setAttribute("myRequestsCount", getUserService().getUserRequestForUserCount(getUserId()));
    	session.setAttribute("myCompetitionsCount", competitionService.getActiveCompetitionsByUserCount(getUserId()));
    	session.setAttribute("mySettledCompetitionCount", competitionService.getSettledCompetitionsByUserCount(getUserId()));
    	session.setAttribute("myNewCompetitionCount", competitionService.getNewCompetitionsByUserCount(getUserId()));
    	Integer ongoingCompetitionCount = competitionService.getOngoingCompetitionsByUserCount(getUserId());
    	session.setAttribute("myOngoingCompetitionsCount", ongoingCompetitionCount);
    	Integer invitationsCount = competitionService.getInvitationsForUserCount(getUserId());
    	session.setAttribute("myInvitationsCount", new Integer(ongoingCompetitionCount.intValue() + invitationsCount.intValue()));
    	session.setAttribute("topPublicCompetitions", competitionService.getTopPublicCompetitionsByTurnover(5));

    	model.addAttribute("activitiesList", activities);    	
    	model.addAttribute("currentPage", pageId);
    	model.addAttribute("numberOfPages", activityService.getActivitiesPageCountForUserProfile(getUserProfile(), null));
		
		return "userHomepageView";
	}
	
	@RequestMapping(value="/user/images/{userId}")	
	public void getImage(@PathVariable String userId, HttpServletRequest request, HttpServletResponse response) {
    	String path = getAppRoot() + "images" + File.separator + "users";
    	File imageFile = new File(path, userId + ".jpg");
    	if (!imageFile.exists()) {
    		imageFile = new File(path, "empty.jpg");
    	} 
    	
    	BufferedImage image;
		try {
			image = ImageIO.read(imageFile);
			response.setContentType("image/jpeg");
			ImageIO.write(image, "jpg", response.getOutputStream());
		} catch (IOException ex) {
    		logger.error("Could not get image", ex);
		}
	}
	


}
