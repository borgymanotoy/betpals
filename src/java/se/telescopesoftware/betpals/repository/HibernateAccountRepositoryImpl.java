package se.telescopesoftware.betpals.repository;

import java.util.Collection;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import se.telescopesoftware.betpals.domain.Account;

@Repository
public class HibernateAccountRepositoryImpl implements AccountRepository {

	private SessionFactory sessionFactory;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	@SuppressWarnings("unchecked")
	public Collection<Account> loadUserAccounts(Long userId) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from Account a where a.ownerId = :userId");
    	query.setLong("userId", userId);
		return query.list();
	}

	public Account loadAccount(Long accountId) {
    	Session session = sessionFactory.getCurrentSession();
		return (Account) session.get(Account.class, accountId);
	}

	public Account storeAccount(Account account) {
    	Session session = sessionFactory.getCurrentSession();
		return (Account) session.merge(account);
	}

	public Account loadUserAccountForCurrency(Long userId, String currency) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from Account a where a.ownerId = :userId and a.currency = :currency");
    	query.setLong("userId", userId);
    	query.setString("currency", currency);
    	Iterator<?> iterator = query.iterate();
    	return (Account) (iterator.hasNext() ? iterator.next() : null);
	}

	public void setAsDefault(Account account) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("update Account set defaultAccount = false where ownerId = :ownerId");
    	query.setLong("ownerId", account.getOwnerId());
    	query.executeUpdate();
		session.saveOrUpdate(account);
	}

}
