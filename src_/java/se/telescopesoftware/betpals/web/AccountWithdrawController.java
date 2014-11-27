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
public class AccountWithdrawController extends AbstractPalsController {

	private AccountService accountService;

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@RequestMapping(value="/accountwithdrawview")	
	public String getView(@RequestParam("accountId") Long accountId, Model model) {
    	model.addAttribute("accountId", accountId);
		
		return "accountWithdrawView";
	}
	
	@RequestMapping(value="/accountwithdraw")	
	public String withdrawFromAccount(@RequestParam("accountId") Long accountId, @RequestParam("amount") BigDecimal amount, Model model, HttpSession session) {
		//TODO: Move to service when withdraw logic will be defined 
		Account account = accountService.getAccount(accountId);
    	if (account.getOwnerId().equals(getUserId()) ) {
			AccountTransaction transaction = new AccountTransaction(account, amount.negate(), AccountTransactionType.WITHDRAW);
			account.addTransaction(transaction);
			
			account = accountService.saveAccount(account);
			model.addAttribute("account", account);
	    	session.setAttribute("accounts", accountService.getUserAccounts(getUserId()));
	    	logger.info(getUser() + " has withdraw " + amount + " from " + account);
	    	logUserAction("Withdraw " + amount + " from " + account);
	    	
			return "accountDetailsView";
    	}
    	
		return "accountWithdrawView";
	}
	
	@RequestMapping(value="/admin/accountwithdraw")	
	public String withdrawFromAccountByAdmin(@RequestParam("accountId") Long accountId, @RequestParam("amount") BigDecimal amount, Model model, HttpSession session) {
		//TODO: Move to service when withdraw logic will be defined 
		Account account = accountService.getAccount(accountId);
		AccountTransaction transaction = new AccountTransaction(account, amount.negate(), AccountTransactionType.WITHDRAW);
		account.addTransaction(transaction);
			
		//TODO: Add check for available funds
		account = accountService.saveAccount(account);
		model.addAttribute("accountId", accountId);
		logger.info(getUser() + " has withdraw " + amount + " from " + account);
			
		return "accountDetailsAdminAction";
	}
	
	
	
}
