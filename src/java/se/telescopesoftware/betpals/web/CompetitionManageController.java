package se.telescopesoftware.betpals.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	public String getView(Model model) {

		model.addAttribute("competitionList", competitionService.getActiveCompetitionsByUser(getUserId()));
		
		return "manageCompetitionsView";
	}
	
	
}
