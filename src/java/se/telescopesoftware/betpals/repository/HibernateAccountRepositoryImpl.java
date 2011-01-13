package se.telescopesoftware.betpals.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import se.telescopesoftware.betpals.domain.Account;

public class HibernateAccountRepositoryImpl extends HibernateDaoSupport implements AccountRepository {

	@SuppressWarnings("unchecked")
	public Collection<Account> loadUserAccounts(Long userId) {
		return getHibernateTemplate().find("from Account a where a.ownerId = ?", userId);
	}

	public Account loadAccount(Long accountId) {
		return getHibernateTemplate().get(Account.class, accountId);
	}

	public void storeAccount(Account account) {
		getHibernateTemplate().saveOrUpdate(account);
	}

	public Account loadUserAccountForCurrency(Long userId, String currency) {
		List result = getHibernateTemplate().findByNamedParam("from Account a where a.ownerId = :userId and a.currency = :currency",
				new String [] {"userId", "currency"},
				new Object [] {userId, currency});
		if (result != null) {
			return (Account) result.get(0);
		}
		return null;
	}

}
