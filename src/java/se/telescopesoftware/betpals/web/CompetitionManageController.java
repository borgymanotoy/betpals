package se.telescopesoftware.betpals.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.services.CompetitionService;

@Controller
public class CompetitionManageController extends AbstractPalsController {
	
	private CompetitionService competitionService;
	
    
	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}
	
	@RequestMapping(value="/managecompetitions")	
	public String getCompetitionsListView(Model model, HttpSession session) {

		model.addAttribute("competitionList", competitionService.getActiveCompetitionsByUser(getUserId()));
		model.addAttribute("settledCompetitionCount", competitionService.getSettledCompetitionsByUserCount(getUserId()));
    	session.setAttribute("myCompetitionsCount", competitionService.getActiveCompetitionsByUserCount(getUserId()));

		return "manageCompetitionsView";
	}
	
	@RequestMapping(value="/settledcompetitions")	
	public String getSettledCompetitionsListView(Model model, HttpSession session) {
		
		model.addAttribute("competitionList", competitionService.getSettledCompetitionsByUser(getUserId()));
		return "settledCompetitionsView";
	}

	@RequestMapping(value="/settledcompetition")	
	public String getSettledCompetitionView(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		model.addAttribute(competition);

		return "settledCompetitionView";
	}
	
	
	@RequestMapping(value="/ongoingcompetitions")	
	public String getOngoingCompetitionsListView(Model model, HttpSession session) {
		model.addAttribute("competitionList", competitionService.getOngoingCompetitionsByUser(getUserId()));
		
		return "ongoingCompetitionsView";
	}
	
	@RequestMapping(value="/ongoingcompetition")	
	public String getOngoingCompetitionView(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		model.addAttribute(competition);

		return "ongoingCompetitionView";
	}
	
	@RequestMapping(value="/managecompetition")	
	public String getManageCompetitionView(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		model.addAttribute(competition);
		
		return "manageCompetitionView";
	}
	
	@RequestMapping(value="/deletecompetition")	
	public String deleteCompetition(@RequestParam("competitionId") Long competitionId, Model model) {
		competitionService.deleteCompetition(competitionId);
		
		return "manageCompetitionsAction";
	}
	
	@RequestMapping(value="/voidalternative")	
	public String voidAlternative(@RequestParam("competitionId") Long competitionId, @RequestParam("alternativeId") Long alternativeId, Model model) {
		competitionService.voidAlternative(competitionId, alternativeId);
		
		Competition competition = competitionService.getCompetitionById(competitionId);
		model.addAttribute(competition);

		return "manageCompetitionView";
	}
	
	@RequestMapping(value="/settlecompetition")	
	public String settleCompetition(@RequestParam("competitionId") Long competitionId, @RequestParam("alternativeId") Long alternativeId, Model model) {
		competitionService.settleCompetition(competitionId, alternativeId);
		
		return "manageCompetitionsAction";
	}
	
	@RequestMapping(value="/updatecompetition")	
	public String inviteToCompetition(@ModelAttribute("competition") Competition competition, BindingResult result, Model model) {
		if (result.hasErrors()) {
			logger.debug("Error found: " + result.getErrorCount());
			return "manageCompetitionView";
		}
		
		Competition storedCompetition = competitionService.getCompetitionById(competition.getId());
		storedCompetition.setDeadline(competition.getDeadline());
		storedCompetition.setSettlingDeadline(competition.getSettlingDeadline());
		competitionService.saveCompetition(storedCompetition);
		
		
		return "manageCompetitionsAction";
	}

	
}
