package se.telescopesoftware.betpals.web.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.services.CompetitionService;
import se.telescopesoftware.betpals.web.AbstractPalsController;


@Controller
public class CompetitionsController extends AbstractPalsController {

    private CompetitionService competitionService;

    @Autowired
    public void setCompetitionService(CompetitionService competitionService) {
    	this.competitionService = competitionService;
    }
    
    
	@RequestMapping("/admin/listcompetitions")
	public String listUsers(Model model) {
		Collection<Competition> competitions = competitionService.getAllCompetitions(null, null);
    	model.addAttribute("competitionList", competitions);
		return "competitionListView";
	}

   
	@RequestMapping(value="/admin/viewcompetition")	
	public String getAdminCompetitionView(@RequestParam("competitionId") Long competitionId, Model model) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		model.addAttribute(competition);
		model.addAttribute("competitionLog", competitionService.getCompetitionLogEntries(competitionId));
		
		return "adminCompetitionView";
	}
	
	@RequestMapping(value="/admin/processexpired")	
	public String processExpired(Model model) {
		competitionService.processExpiredCompetitions();
		Collection<Competition> competitions = competitionService.getAllCompetitions(null, null);
    	model.addAttribute("competitionList", competitions);
		return "competitionListView";
	}
	
	
	
}
