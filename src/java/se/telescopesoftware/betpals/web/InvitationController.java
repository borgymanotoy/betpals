package se.telescopesoftware.betpals.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityType;
import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.Invitation;
import se.telescopesoftware.betpals.services.ActivityService;
import se.telescopesoftware.betpals.services.CompetitionService;

@Controller
public class InvitationController extends AbstractPalsController {

	private CompetitionService competitionService;
	private ActivityService activityService;

	
	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}
	
    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }
	
	@RequestMapping(value="/invitations", method = RequestMethod.GET)
	public String showActiveInvitations(Model model) {
		
		model.addAttribute("invitationList", competitionService.getInvitationsForUser(getUserId()));
		return "activeInvitationsView";
	}
	
	@RequestMapping(value="/invitation", method = RequestMethod.POST)
	public String viewInvitation(@RequestParam("invitationId") Long invitationId, Model model) {
		
		Invitation invitation = competitionService.getInvitationById(invitationId);
		model.addAttribute("invitation", invitation);
		model.addAttribute("competition", competitionService.getCompetitionById(invitation.getCompetitionId()));
		return "invitationView";
	}
	
	@RequestMapping(value="/placebet", method = RequestMethod.POST)
	public String placeBet(@RequestParam("invitationId") Long invitationId, @RequestParam("alternativeId") Long alternativeId, Model model) {
		
		Invitation invitation = competitionService.getInvitationById(invitationId);
		Competition competition = competitionService.getCompetitionById(invitation.getCompetitionId());

    	Bet bet = new Bet();
    	bet.setOwnerId(getUserId());
    	bet.setCurrency(competition.getCurrency());
    	bet.setStake(competition.getFixedStake());
    	bet.setDetails(competition.getName());
    	bet.setSelectionId(alternativeId);
    	bet.setPlaced(new Date());
    	competitionService.placeBet(bet);
    	competitionService.deleteInvitation(invitationId);
    	
    	Activity activity = new Activity();
    	activity.setCreated(new Date());
    	activity.setOwnerId(getUserId());
    	activity.setOwnerName(getUserProfile().getFullName());
    	activity.setActivityType(ActivityType.MESSAGE);
    	activity.setMessage("Joined the competition: " + competition.getName());
    	
    	activityService.addActivity(activity);

		return "userHomepageAction";
	}
	
}
