package se.telescopesoftware.betpals.services;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.repository.AccountRepository;

public class AccountServiceImpl implements AccountService {

	private Map<String, String> supportedCurrencies = new HashMap<String, String>();
	private AccountRepository accountRepository;
	
    private static Logger logger = Logger.getLogger(AccountServiceImpl.class);
	
	
	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	public void setSupportedCurrenciesMap(Map<String, String> currencies) {
		this.supportedCurrencies = currencies;
	}

	public Collection<String> getSupportedCurrencies() {
		return supportedCurrencies.values();
	}
	
	public Collection<Account> getUserAccounts(Long userId) {
		return accountRepository.loadUserAccounts(userId);
	}

	public Account getAccount(Long accountId) {
		return accountRepository.loadAccount(accountId);
	}

	public void saveAccount(Account account) {
		accountRepository.storeAccount(account);
	}

	public void depositToAccount(Long accountId, BigDecimal amount) {
		// TODO Auto-generated method stub
		
	}

	public void withdrawFromAccount(Long accountId, BigDecimal amount) {
		// TODO Auto-generated method stub
		
	}

	public Account getUserAccountForCurrency(Long userId, String currency) {
		return accountRepository.loadUserAccountForCurrency(userId, currency);
	}

}
