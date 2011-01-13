package se.telescopesoftware.betpals.repository;

import java.util.Collection;

import se.telescopesoftware.betpals.domain.Account;

public interface AccountRepository {

	Collection<Account> loadUserAccounts(Long userId);
	
	Account loadAccount(Long accountId);
	
	void storeAccount(Account account);
	
	Account loadUserAccountForCurrency(Long userId, String currency);
}
