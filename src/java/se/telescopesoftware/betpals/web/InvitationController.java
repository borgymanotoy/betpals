package se.telescopesoftware.betpals.web;

import java.math.BigDecimal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.Event;
import se.telescopesoftware.betpals.domain.Invitation;
import se.telescopesoftware.betpals.services.CompetitionService;

@Controller
public class InvitationController extends AbstractCompetitionController {

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
	public String viewInvitation(@RequestParam(value="invitationId", defaultValue="-1", required=false) Long invitationId, @RequestParam("competitionId") Long competitionId, Model model) {

		Invitation invitation = null;
		Competition competition = null;

		if (invitationId >= 0) {
			invitation = competitionService.getInvitationById(invitationId);
			competition = competitionService.getCompetitionById(invitation.getCompetitionId());
		} else {
			competition = competitionService.getCompetitionById(competitionId);
			invitation = new Invitation(competition, getUserId());
		}

		for(Event e : competition.getEvents()){
			e.setAlternatives(sortAlternativeSet(e.getSortedAlternatives()));
		}

		model.addAttribute("invitation", invitation);
		model.addAttribute("competition", competition);

		return "invitationView";
	}

	@RequestMapping(value="/linkinvitation")
	public String viewLinkInvitation(@RequestParam("competitionId") Long competitionId, @RequestParam(value="alternativeId", defaultValue="0", required=false) Long alternativeId, @RequestParam(value="betValue", defaultValue="0", required=false) BigDecimal betValue, Model model, HttpSession session) {

		Competition competition = competitionService.getCompetitionById(competitionId);
		Invitation invitation = new Invitation(competition, getUserId());

		for(Event e : competition.getEvents()){
			e.setAlternatives(sortAlternativeSet(e.getSortedAlternatives()));
		}

		model.addAttribute("invitation", invitation);
		model.addAttribute("competition", competition);

		model.addAttribute("alternativeId", alternativeId);
		model.addAttribute("betValue", betValue.toString());

		return "invitationView";
	}
}
