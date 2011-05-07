package se.telescopesoftware.betpals.web;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.Activity;
import se.telescopesoftware.betpals.domain.ActivityType;
import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.Group;
import se.telescopesoftware.betpals.domain.QuickCompetition;
import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.ActivityService;
import se.telescopesoftware.betpals.services.CompetitionService;
import se.telescopesoftware.betpals.services.FacebookService;
import se.telescopesoftware.betpals.services.UserService;

@Controller
public class CompetitionQuickController extends AbstractPalsController {
	
	private CompetitionService competitionService;
	private AccountService accountService;
	private ActivityService activityService;
	private FacebookService facebookService;
	private UserService userService;
    
    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Autowired
	public void setFacebookService(FacebookService facebookService) {
		this.facebookService = facebookService;
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
    	model.addAttribute("quickCompetition", competition);
    	model.addAttribute("groupList", userService.getUserGroups(getUserId()));
    	model.addAttribute("communityList", userService.getUserCommunities(getUserId()));
    	model.addAttribute("friendList", getUserProfile().getFriends());
		
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
    	
    	Set<Long> friendsIdSet = new HashSet<Long>();
   	
    	if (quickCompetition.isAllFriends()) {
    		friendsIdSet.addAll(getUserProfile().getFriendsIdSet());
    	} else {
    		friendsIdSet.addAll(quickCompetition.getFriendsIdSet());
    		Set<Long> groupIdSet = quickCompetition.getGroupIdSet();
    		for (Long groupId : groupIdSet) {
    			Group group = userService.getGroupById(groupId);
    			friendsIdSet.addAll(group.getMembersIdSet());
    		}
    	}
    	
    	competitionService.sendInvitationsToFriends(competition, friendsIdSet, getUserProfile());

    	Activity activity = new Activity(getUserProfile(), ActivityType.USER);
    	activity.setMessage("Created new competition: " + competition.getName());
    	activityService.saveActivity(activity);

    	if (quickCompetition.isFacebookPublish()) {
    		facebookService.postCompetitionToUserWall(competition, getUserProfile());
    	}
    	
		return "userHomepageAction";
	}

	@RequestMapping(value="/competition/images/{competitionId}")	
	public void getImage(@PathVariable String competitionId, HttpServletResponse response) {
    	sendJPEGImage(IMAGE_FOLDER_COMPETITIONS, competitionId, response);
	}
	
	
}
