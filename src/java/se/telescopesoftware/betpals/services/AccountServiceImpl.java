package se.telescopesoftware.betpals.services;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.AccountTransaction;
import se.telescopesoftware.betpals.domain.AccountTransactionType;
import se.telescopesoftware.betpals.repository.AccountRepository;

public class AccountServiceImpl implements AccountService {

	private Map<String, String> supportedCurrencies = new HashMap<String, String>();
	private AccountRepository accountRepository;
	private SiteConfigurationService siteConfigurationService;
	
    private static Logger logger = Logger.getLogger(AccountServiceImpl.class);
	
	
    public void setSiteConfigurationService(SiteConfigurationService siteConfigurationService) {
    	this.siteConfigurationService = siteConfigurationService;
    }
    
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

	public Account saveAccount(Account account) {
		logger.debug("Saving " + account);
		return accountRepository.storeAccount(account);
	}

	public void depositToAccount(Long accountId, BigDecimal amount) {
		// TODO Auto-generated method stub
		
	}

	public void withdrawFromAccount(Long accountId, BigDecimal amount) {
		// TODO Auto-generated method stub
		
	}

	public Account getUserAccountForCurrency(Long userId, String currency) {
		return accountRepository.loadUserAccountForCurrency(userId, currency);
		//TODO: Add pagination to account transaction list
	}

	public String getDefaultCurrency() {
		return supportedCurrencies.get("default");
	}

	public Account createDefaultAccountForUser(Long userId) {
		logger.info("Creating default account for user " + userId);
		Account account = saveAccount(new Account(getDefaultCurrency(), userId));
		account.setDefaultAccount(true);
		String defaultAmount = siteConfigurationService.getParameterValue("defaultAccountAmount");
		AccountTransaction transaction = new AccountTransaction(account, new BigDecimal(defaultAmount), AccountTransactionType.DEPOSIT);
		account.addTransaction(transaction);
		logger.info("Depositing default amount (" + transaction + ")");
		
		return saveAccount(account);
	}

	public void setAsDefault(Account account) {
		logger.info("Setting as default " + account);
		account.setDefaultAccount(true);
		accountRepository.setAsDefault(account);
	}

}
