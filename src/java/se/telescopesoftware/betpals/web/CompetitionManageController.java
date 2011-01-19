package se.telescopesoftware.betpals.web;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.services.CompetitionService;

@Controller
public class CompetitionManageController extends AbstractPalsController {
	
	private CompetitionService competitionService;
	
    private static Logger logger = Logger.getLogger(CompetitionManageController.class);

    
	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}
	
	@RequestMapping(value="/managecompetitions", method = RequestMethod.GET)	
	public String getCompetitionsListView(Model model, HttpSession session) {

		model.addAttribute("competitionList", competitionService.getActiveCompetitionsByUser(getUserId()));
    	session.setAttribute("myCompetitionsCount", competitionService.getActiveCompetitionsByUserCount(getUserId()));

		return "manageCompetitionsView";
	}
	
	@RequestMapping(value="/managecompetition", method = RequestMethod.POST)	
	public String getManageCompetitionView(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		model.addAttribute(competition);

		return "manageCompetitionView";
	}
	
	@RequestMapping(value="/deletecompetition", method = RequestMethod.POST)	
	public String deleteCompetition(@RequestParam("competitionId") Long competitionId, Model model) {
		competitionService.deleteCompetition(competitionId);
		
		return "manageCompetitionsAction";
	}
	
	@RequestMapping(value="/voidalternative", method = RequestMethod.POST)	
	public String voidAlternative(@RequestParam("competitionId") Long competitionId, @RequestParam("alternativeId") Long alternativeId, Model model) {
		competitionService.voidAlternative(competitionId, alternativeId);
		
		Competition competition = competitionService.getCompetitionById(competitionId);
		model.addAttribute(competition);

		return "manageCompetitionView";
	}
	
	@RequestMapping(value="/settlecompetition", method = RequestMethod.POST)	
	public String settleCompetition(@RequestParam("competitionId") Long competitionId, Model model) {
		competitionService.settleCompetition(competitionId);
		
		return "manageCompetitionsAction";
	}
	
	@RequestMapping(value="/updatecompetition", method = RequestMethod.POST)	
	public String inviteToCompetition(@ModelAttribute("competition") Competition competition, BindingResult result, Model model) {
		if (result.hasErrors()) {
			logger.debug("Error found: " + result.getErrorCount());
			return "manageCompetitionView";
		}
		
		Competition storedCompetition = competitionService.getCompetitionById(competition.getId());
		storedCompetition.setDeadline(competition.getDeadline());
		storedCompetition.setSettlingDeadline(competition.getSettlingDeadline());
		competitionService.addCompetition(storedCompetition);
		
		
		return "manageCompetitionsAction";
	}

	
}
