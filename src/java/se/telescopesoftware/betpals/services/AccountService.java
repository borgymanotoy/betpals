package se.telescopesoftware.betpals.services;

import java.math.BigDecimal;
import java.util.Collection;

import se.telescopesoftware.betpals.domain.Account;

public interface AccountService {

	
	Collection<String> getSupportedCurrencies();
	
	Collection<Account> getUserAccounts(Long userId);
	
	Account getAccount(Long accountId);
	
	Account getUserAccountForCurrency(Long userId, String currency);
	
	void saveAccount(Account account);
	
	void depositToAccount(Long accountId, BigDecimal amount);
	
	void withdrawFromAccount(Long accountId, BigDecimal amount);
}
