package se.telescopesoftware.betpals.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.Bet;
import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.QuickCompetition;
import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.CompetitionService;
import se.telescopesoftware.betpals.utils.ThumbnailUtil;

@Controller
public class CompetitionQuickController extends AbstractPalsController {
	
	private CompetitionService competitionService;
	private AccountService accountService;
    private String appRoot;

    private static Logger logger = Logger.getLogger(CompetitionQuickController.class);

    
	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

    @Autowired
    public void setAppRoot(String appRoot) {
    	this.appRoot = appRoot;
    }

	@RequestMapping(value="/quickcompetitionview", method = RequestMethod.POST)	
	public String getView(@RequestParam("accountId") Long accountId, @RequestParam("stake") BigDecimal stake, @RequestParam("alternative") String alternative, Model model) {
		QuickCompetition competition = new QuickCompetition();
		competition.setName(alternative);
		competition.setStake(stake);
		competition.setAccountId(accountId);
    	model.addAttribute("quickCompetition", competition);
		
		return "quickCompetitionView";
	}
	
	@RequestMapping(value="/quickcompetition", method = RequestMethod.POST)	
	public String processSubmit(@Valid QuickCompetition quickCompetition, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		logger.debug("Error found: " + result.getErrorCount());
    		return "quickCompetitionView";
    	}
    	
    	Competition competition = quickCompetition.createCompetition();
    	competition.setOwnerId(getUserId());
    	Account account = accountService.getAccount(quickCompetition.getAccountId());
    	competition.setCurrency(account.getCurrency());
    	
    	competition = competitionService.addCompetition(competition);
    	saveImage(quickCompetition.getImageFile(), competition.getId());
    	
    	Bet bet = new Bet();
    	bet.setOwnerId(getUserId());
    	bet.setAccountId(quickCompetition.getAccountId());
    	bet.setStake(quickCompetition.getStake());
    	bet.setDetails(quickCompetition.getName());
    	bet.setSelectionId(competition.getOwnerAlternativeId());
    	bet.setPlaced(new Date());
    	competitionService.placeBet(bet);
    	
    	if (quickCompetition.isAllFriends()) {
    		competitionService.sendInvitationsToFriends(competition, getUserProfile().getFriendsIdSet(), getUserProfile());
    	} else {
    		competitionService.sendInvitationsToFriends(competition, quickCompetition.getFriendsIdSet(), getUserProfile());
    	}
    	
    	//TODO: Implement group invitation
    	quickCompetition.getGroupIdSet();

    	//TODO: Implement facebook publishing of quick competition
    	quickCompetition.isFacebookPublish();
    	
		return "userHomepageAction";
	}

	@RequestMapping(value="/competition/images/{competitionId}", method = RequestMethod.GET)	
	public void getImage(@PathVariable String competitionId, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Get image for competition: " + competitionId);

    	String path = appRoot + "images" + File.separator + "competitions";
    	File imageFile = new File(path, competitionId + ".jpg");
    	if (!imageFile.exists()) {
    		imageFile = new File(path, "empty.jpg");
    	} 
    	
    	BufferedImage image;
		try {
			image = ImageIO.read(imageFile);
			response.setContentType("image/jpeg");
			ImageIO.write(image, "jpg", response.getOutputStream());
		} catch (IOException ex) {
    		logger.error("Could not get image", ex);
		}
	}
	
    private void saveImage(MultipartFile imageFile, Long competitionId) {
        if (imageFile != null && !imageFile.isEmpty()) {
        	try {
	        	InputStream inputStream = imageFile.getInputStream();
	        	BufferedImage image = ImageIO.read(inputStream);
	        	BufferedImage thumbnailImage = ThumbnailUtil.getScaledInstance(image, 50, 50);
	
	        	String path = appRoot + "images" + File.separator + "competitions";
	        	File outputFile = new File(path, competitionId + ".jpg");
	        	ImageIO.write(thumbnailImage, "jpg", outputFile);
	        	logger.debug("Writing competition picture to " + outputFile.getPath());

        	} catch(Exception ex) {
        		logger.error("Could not save image", ex);
        	}
        }
    }

	
}
