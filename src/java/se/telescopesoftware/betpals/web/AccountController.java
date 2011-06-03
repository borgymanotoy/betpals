package se.telescopesoftware.betpals.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.services.AccountService;
import se.telescopesoftware.betpals.services.CompetitionService;

@Controller
public class AccountController extends AbstractPalsController {

	private AccountService accountService;
	private CompetitionService competitionService;
	
	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@Autowired
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}
	
	@RequestMapping(value="/addaccount", method=RequestMethod.GET)	
	public String addNewAccount(ModelMap model) {
		Collection<String> currencies = accountService.getSupportedCurrencies();
    	model.addAttribute("account", new Account());
    	model.addAttribute("supportedCurrencies", filterExistingAccountCurrencies(currencies));
		
		return "addAccountView";
	}
	
	@RequestMapping(value="/addaccount", method=RequestMethod.POST)	
	public String saveNewAccount(@Valid Account account, BindingResult result, Model model, HttpSession session) {
    	if (result.hasErrors()) {
    		model.addAttribute(result.getAllErrors());
    		return "addAccountView";
    	}

    	account.setCreated(new Date());
    	account.setOwnerId(getUser().getId());
    	//TODO: Add check for existing account with same currency
    	accountService.saveAccount(account);
    	
    	model.addAttribute(account);
    	session.setAttribute("accounts", accountService.getUserAccounts(getUser().getId()));
    	
		return "accountDetailsView";
	}
	
	@RequestMapping(value="/accountdetails")	
	public String getAccount(@RequestParam("accountId") Long accountId, Model model) {
    	Account account = accountService.getAccount(accountId);
    	if (account.getOwnerId().equals(getUserId()) ) {
    		model.addAttribute("account", account);
    		model.addAttribute("activeBets", competitionService.getActiveBetsByUserAndAccount(getUserId(), accountId));
    		model.addAttribute("settledBets", competitionService.getSettledBetsByUserAndAccount(getUserId(), accountId));
    	}
    	
		return "accountDetailsView";
	}
	
	@RequestMapping(value="/accountsetdefault")	
	public String makeDefault(@RequestParam("accountId") Long accountId, Model model, HttpSession session) {
		Account account = accountService.getAccount(accountId);
    	if (account.getOwnerId().equals(getUserId()) ) {
			accountService.setAsDefault(account);
			model.addAttribute("account", account);
    		model.addAttribute("activeBets", competitionService.getActiveBetsByUserAndAccount(getUserId(), accountId));
    		model.addAttribute("settledBets", competitionService.getSettledBetsByUserAndAccount(getUserId(), accountId));
	    	session.setAttribute("accounts", accountService.getUserAccounts(getUserId()));
    	}
    	
		return "accountDetailsView";
	}
	
	private Collection<String> filterExistingAccountCurrencies(Collection<String> currencies) {
		Collection<Account> userAccounts = accountService.getUserAccounts(getUser().getId());
		Collection<String> filteredCurrencies = new ArrayList<String>();
		filteredCurrencies.addAll(currencies);
		for(Account account : userAccounts) {
			filteredCurrencies.remove(account.getCurrency());
		}
		
		return filteredCurrencies;
	}
	
}
