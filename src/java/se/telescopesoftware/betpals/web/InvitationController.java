package se.telescopesoftware.betpals.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.Invitation;
import se.telescopesoftware.betpals.services.CompetitionService;

@Controller
public class InvitationController extends AbstractPalsController {

	private CompetitionService competitionService;

	
	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
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
	
	
}
