package se.telescopesoftware.betpals.web;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityType;
import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.CompetitionType;
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
	
	@RequestMapping(value="/invitations")
	public String showActiveInvitations(Model model) {
		
		model.addAttribute("invitationList", competitionService.getInvitationsForUser(getUserId()));
		model.addAttribute("competitionList", competitionService.getOngoingCompetitionsByUser(getUserId()));

		return "activeInvitationsView";
	}
	
	@RequestMapping(value="/invitation")
	public String viewInvitation(@RequestParam("invitationId") Long invitationId, Model model) {
		
		Invitation invitation = competitionService.getInvitationById(invitationId);
		model.addAttribute("invitation", invitation);
		model.addAttribute("competition", competitionService.getCompetitionById(invitation.getCompetitionId()));
		return "invitationView";
	}
	
	@RequestMapping(value="/linkinvitation")
	public String viewLinkInvitation(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		Invitation invitation = new Invitation(competition, getUserId());
		model.addAttribute("invitation", invitation);
		model.addAttribute("competition", competition);
		return "invitationView";
	}
	
	@RequestMapping(value="/placebet")
	public String placeBet(@RequestParam("invitationId") Long invitationId, @RequestParam("alternativeId") Long alternativeId, @RequestParam(value="stake", required=false) BigDecimal stake, Model model) {
		
		Invitation invitation = competitionService.getInvitationById(invitationId);
		Competition competition = competitionService.getCompetitionById(invitation.getCompetitionId());

    	Bet bet = new Bet();
    	bet.setOwnerId(getUserId());
    	bet.setOwnerName(getUserProfile().getFullName());
    	bet.setCurrency(competition.getCurrency());
    	if (competition.getCompetitionType() == CompetitionType.POOL_BETTING) {
    		bet.setStake(stake); //TODO: add validation
    	} else {
    		bet.setStake(competition.getFixedStake());
    	}
    	bet.setDetails(competition.getName());
    	bet.setSelectionId(alternativeId);
    	bet.setPlaced(new Date());
    	competitionService.placeBet(bet);
    	competitionService.deleteInvitation(invitationId);
    	
    	Activity activity = new Activity(getUserProfile(), ActivityType.USER);
    	activity.setMessage("Joined the competition: " + competition.getName());
    	
    	activityService.saveActivity(activity);

		return "userHomepageAction";
	}
	
	@RequestMapping(value="/placeanotherbet")
	public String placeAnotherBet(@RequestParam("alternativeId") Long alternativeId, @RequestParam("competitionId") Long competitionId, @RequestParam(value="stake", required=false) BigDecimal stake, Model model) {
	
		Competition competition = competitionService.getCompetitionById(competitionId);
		
		Bet bet = new Bet();
		bet.setOwnerId(getUserId());
		bet.setOwnerName(getUserProfile().getFullName());
		bet.setCurrency(competition.getCurrency());
		if (competition.getCompetitionType() == CompetitionType.POOL_BETTING) {
			bet.setStake(stake); //TODO: add validation
		} else {
			bet.setStake(competition.getFixedStake());
		}
		bet.setDetails(competition.getName());
		bet.setSelectionId(alternativeId);
		bet.setPlaced(new Date());
		competitionService.placeBet(bet);
		
//		Activity activity = new Activity();
//		activity.setCreated(new Date());
//		activity.setOwnerId(getUserId());
//		activity.setOwnerName(getUserProfile().getFullName());
//		activity.setActivityType(ActivityType.MESSAGE);
//		activity.setMessage("Joined the competition: " + competition.getName());
		
//		activityService.addActivity(activity);
		
		return "userHomepageAction";
	}
	
}
