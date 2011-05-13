package se.telescopesoftware.betpals.web;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.InvitationHelper;
import se.telescopesoftware.betpals.domain.QuickCompetition;
import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.CompetitionService;
import se.telescopesoftware.betpals.services.UserService;

@Controller
public class CompetitionQuickController extends AbstractPalsController {
	
	private CompetitionService competitionService;
	private AccountService accountService;
	private UserService userService;
    

	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	

	@RequestMapping(value="/quickcompetitionview")	
	public String getView(@RequestParam("accountId") Long accountId, @RequestParam("stake") BigDecimal stake, @RequestParam("alternative") String alternative, Model model) {
		QuickCompetition competition = new QuickCompetition();
		competition.setName(alternative);
		competition.setStake(stake);
		competition.setAccountId(accountId);
		
		DateTime now = new DateTime();
		competition.setDeadline(now.plusDays(competitionService.getDefaultDeadlineInterval()).toDate());
		competition.setSettlingDeadline(now.plusDays(competitionService.getDefaultSettlingInterval()).toDate());
		
    	model.addAttribute("quickCompetition", competition);
		
		return "quickCompetitionView";
	}
	
	@RequestMapping(value="/quickcompetition")	
	public String processSubmit(@Valid QuickCompetition quickCompetition, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		logger.debug("Error found: " + result.getFieldErrorCount());
    		return "quickCompetitionView";
    	}
    	
    	Account account = accountService.getAccount(quickCompetition.getAccountId());
    	Competition competition = quickCompetition.createCompetition(getUserId(), account.getCurrency());
    	competition = competitionService.saveCompetition(competition);
    	
    	saveImage(quickCompetition.getImageFile(), IMAGE_FOLDER_COMPETITIONS, competition.getId().toString());
    	
    	Bet bet = quickCompetition.createBet(getUserProfile());
    	bet.setSelectionId(competition.getOwnerAlternativeId());
    	competitionService.placeBet(bet);
    	
		model.addAttribute(competition);
		InvitationHelper invitationHelper = new InvitationHelper();
		invitationHelper.setCompetitionId(competition.getId());
		model.addAttribute(invitationHelper);
    	model.addAttribute("groupList", userService.getUserGroups(getUserId()));
    	model.addAttribute("communityList", userService.getUserCommunities(getUserId()));
    	model.addAttribute("friendList", getUserProfile().getFriends());
		
		return "inviteToCompetitionView";
	}

	@RequestMapping(value="/competition/images/{competitionId}")	
	public void getImage(@PathVariable String competitionId, HttpServletResponse response) {
    	sendJPEGImage(IMAGE_FOLDER_COMPETITIONS, competitionId, response);
	}
	
	
}
