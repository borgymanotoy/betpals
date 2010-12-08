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

@Controller
public class AccountController extends AbstractPalsController {

	private AccountService accountService;
	
	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@RequestMapping(value="/addaccount", method = RequestMethod.GET)	
	public String addNewAccount(ModelMap model) {
		Collection<String> currencies = accountService.getSupportedCurrencies();
    	model.addAttribute("account", new Account());
    	model.addAttribute("supportedCurrencies", filterExistingAccountCurrencies(currencies));
		
		return "addAccountView";
	}
	
	@RequestMapping(value="/addaccount", method = RequestMethod.POST)	
	public String saveNewAccount(@Valid Account account, BindingResult result, Model model, HttpSession session) {
    	if (result.hasErrors()) {
    		logger.debug("Error found: " + result.getErrorCount());
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
	
	@RequestMapping(value="/accountdetails", method = RequestMethod.POST)	
	public String getAccount(@RequestParam("accountId") Long accountId, Model model) {
    	Account account = accountService.getAccount(accountId);
    	logger.debug("Retrieved account for id " + accountId);
    	logger.debug("Account owner id " + account.getOwnerId());
    	if (account.getOwnerId().equals(getUser().getId()) ) {
    		model.addAttribute("account", account);
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
