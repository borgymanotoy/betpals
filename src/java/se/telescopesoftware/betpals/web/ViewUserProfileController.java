package se.telescopesoftware.betpals.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.CompetitionService;

@Controller
public class ViewUserProfileController extends AbstractPalsController {

	private CompetitionService competitionService;

	
	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}

	
    @RequestMapping(value="/viewprofile/{userId}")
    protected String viewUserProfile(@PathVariable("userId") Long userId, ModelMap model) {
    	UserProfile userProfile = getUserService().getUserProfileByUserId(userId);
    	model.addAttribute("userProfile", userProfile);
    	model.addAttribute("totalCompetitions", competitionService.getTotalUserCompetitionsCount(userId));
    	model.addAttribute("totalBets", competitionService.getTotalUserBetsCount(userId));
        return "userProfileView";
    }

}
