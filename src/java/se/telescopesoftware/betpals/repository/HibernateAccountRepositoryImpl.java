package se.telescopesoftware.betpals.repository;

import java.util.Collection;

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

}
