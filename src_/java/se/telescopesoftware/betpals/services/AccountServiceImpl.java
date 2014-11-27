package se.telescopesoftware.betpals.services;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.AccountTransaction;
import se.telescopesoftware.betpals.domain.AccountTransactionType;
import se.telescopesoftware.betpals.repository.AccountRepository;

@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

	private Map<String, String> supportedCurrencies = new HashMap<String, String>();
	private AccountRepository accountRepository;
	private SiteConfigurationService siteConfigurationService;
	
    private static Logger logger = Logger.getLogger(AccountServiceImpl.class);
	
	
    @Autowired
    public void setSiteConfigurationService(SiteConfigurationService siteConfigurationService) {
    	this.siteConfigurationService = siteConfigurationService;
    }
    
    @Autowired
	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
    @Resource
	public void setSupportedCurrencies(Map<String, String> currencies) {
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

	@Transactional(readOnly = false)
	public Account saveAccount(Account account) {
		logger.debug("Saving " + account);
		return accountRepository.storeAccount(account);
	}

	@Transactional(readOnly = false)
	public void depositToAccount(Long accountId, BigDecimal amount) {
		// TODO Implement after deposit logic will be defined
	}

	@Transactional(readOnly = false)
	public void withdrawFromAccount(Long accountId, BigDecimal amount) {
		// TODO Implement after withdraw logic will be defined
	}

	public Account getUserAccountForCurrency(Long userId, String currency) {
		return accountRepository.loadUserAccountForCurrency(userId, currency);
	}

	public String getDefaultCurrency() {
		return supportedCurrencies.get("default");
	}

	@Transactional(readOnly = false)
	public Account createDefaultAccountForUser(Long userId) {
		logger.info("Creating default account for user " + userId);
		Account account = saveAccount(new Account(getDefaultCurrency(), userId));
		account.setDefaultAccount(true);
		String defaultAmount = siteConfigurationService.getParameterValue("default.account.amount", "100");
		AccountTransaction transaction = new AccountTransaction(account, new BigDecimal(defaultAmount), AccountTransactionType.DEPOSIT);
		account.addTransaction(transaction);
		logger.info("Depositing default amount (" + transaction + ")");
		
		return saveAccount(account);
	}

	@Transactional(readOnly = false)
	public void setAsDefault(Account account) {
		logger.info("Setting as default " + account);
		account.setDefaultAccount(true);
		accountRepository.setAsDefault(account);
	}

}
