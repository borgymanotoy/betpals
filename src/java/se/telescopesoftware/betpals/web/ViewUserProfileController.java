package se.telescopesoftware.betpals.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.CompetitionService;
import se.telescopesoftware.betpals.services.UserService;

@Controller
public class ViewUserProfileController extends AbstractPalsController {

    private UserService userService;
	private CompetitionService competitionService;

	
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}

	
    @RequestMapping(value="/viewprofile/{userId}", method = RequestMethod.GET)
    protected String viewUserProfile(@PathVariable("userId") Long userId, ModelMap model) {
    	UserProfile userProfile = userService.getUserProfileByUserId(userId);
    	model.addAttribute("userProfile", userProfile);
    	model.addAttribute("totalCompetitions", competitionService.getTotalUserCompetitionsCount(userId));
    	model.addAttribute("totalBets", competitionService.getTotalUserBetsCount(userId));
        return "userProfileView";
    }

}
