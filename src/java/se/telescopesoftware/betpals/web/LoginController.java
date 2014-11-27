package se.telescopesoftware.betpals.web;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.User;
import se.telescopesoftware.betpals.domain.UserProfile;
import se.telescopesoftware.betpals.services.CompetitionService;
import se.telescopesoftware.betpals.utils.MyBetPalsJSONContainer;

@Controller
public class LoginController extends AbstractPalsController {
	
    private CompetitionService competitionService;

    @Autowired
    public void setCompetitionService(CompetitionService competitionService) {
    	this.competitionService = competitionService;
    }

    
	@RequestMapping("/login")
	public String get(HttpServletRequest request, HttpServletResponse response, Model model) {

		String strclientOffset = (String) request.getParameter("clientOffset");
		

		DateTime serverDate = new DateTime();
		double serverGMT = getGMTDifference(serverDate.toDate(), null, true);
		double clientOffset = 0.0;
		double serverClientDifference = 0.0;

		MyBetPalsJSONContainer json = new MyBetPalsJSONContainer();
		if (strclientOffset != null) {
			clientOffset = Double.valueOf(strclientOffset).doubleValue();
			serverClientDifference = clientOffset - serverGMT;

			Collection<Competition> publicCompetitions = competitionService.getTopPublicCompetitionsByTurnover(5);

			model.addAttribute("clientServerDifference", new Double(serverClientDifference));
			model.addAttribute("topPublicCompetitions", competitionService.getTopPublicCompetitionsByTurnover(5));
		}

		DateTime clientDate = serverDate.plusMinutes((int)(serverClientDifference * 60));

		logger.info("\n\n\n");
		logger.info("<<< LOGIN >>>");
		logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("[serverDate]: " + serverDate.toString());
		logger.info("[serverGMT]: " + serverGMT);
		logger.info("[clientDate]: " + clientDate.toString());
		logger.info("[clientOffset]: " + clientOffset);
		logger.info("[JSON Array Size]: " + json.getArray().size());
		logger.info("---");
		logger.info("[serverClientDifference]: " + serverClientDifference);
		logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("\n\n\n");

		model.addAttribute(new UserProfile());
		
		return "loginView";
	}
	
	@RequestMapping({"/", "/index"})
	public String getRoot() {

		logger.info("\n\n\n");
		logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("<<< GET-ROOT >>>");
		logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("\n\n\n");

		User user = getUser();
		if (user.isSupervisor()) {
			return "adminHomepageAction";
		}
		return "userHomepageAction";
	}

	@RequestMapping("/processlogin.html")
	public String processLogin() {
		UserProfile userProfile = getUserProfile();
		userProfile.registerVisit();
		getUserService().updateUserProfile(userProfile);
		logger.info("Logged in " + getUser());
		logUserAction("Log in");
		return "indexAction";
	}

}
