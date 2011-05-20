package se.telescopesoftware.betpals.web;

import java.math.BigDecimal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.AccountTransaction;
import se.telescopesoftware.betpals.domain.AccountTransactionType;
import se.telescopesoftware.betpals.services.AccountService;

@Controller
public class AccountDepositController extends AbstractPalsController {

	private AccountService accountService;

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@RequestMapping(value="/accountdepositview")	
	public String getView(@RequestParam("accountId") Long accountId, Model model) {
    	model.addAttribute("accountId", accountId);
		
		return "accountDepositView";
	}
	
	//TODO: Add transaction (not account transaction) around deposit method	
	@RequestMapping(value="/accountdeposit")	
	public String depositToAccount(@RequestParam("accountId") Long accountId, @RequestParam("amount") BigDecimal amount, Model model, HttpSession session) {
		//TODO: Add parameter validation
		//TODO: Add validation for account
		//TODO: Move to service?
		Account account = accountService.getAccount(accountId);
    	if (account.getOwnerId().equals(getUserId()) ) {
			AccountTransaction transaction = new AccountTransaction(account, amount, AccountTransactionType.DEPOSIT);
			account.addTransaction(transaction);
			
			//TODO: Add check for correct currency
			account = accountService.saveAccount(account);
			model.addAttribute("account", account);
	    	session.setAttribute("accounts", accountService.getUserAccounts(getUserId()));

			return "accountDetailsView";
    	}
    	
		return "accountDepositView";
	}
	
	
	
}
