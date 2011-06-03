package se.telescopesoftware.betpals.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Represents a money account of user.
 * User can have several accounts in different currencies.
 *
 */
@Entity
@Table(name="account")
public class Account implements Serializable {

	private static final long serialVersionUID = 6936648377538950243L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long ownerId;
	private String currency;
	private BigDecimal balance = new BigDecimal("0.00");
	private BigDecimal available = new BigDecimal("0.00");
	private Date created;
	private boolean active = true;
	private boolean defaultAccount = false;
	
	//TODO: Check cascade type for possible side effects
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="join_account_transaction",
            joinColumns = @JoinColumn(name="account_id"),
            inverseJoinColumns = @JoinColumn(name="transaction_id")
    )
	private List<AccountTransaction> transactions = new ArrayList<AccountTransaction>();

	
	public Account() {
	}
	
	public Account(String currency, Long ownerId) {
		this.created = new Date();
		this.currency = currency;
		this.ownerId = ownerId;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getAvailable() {
		return available;
	}

	public void setAvailable(BigDecimal available) {
		this.available = available;
	}

	public List<AccountTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<AccountTransaction> transactions) {
		this.transactions = transactions;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public boolean isActive() {
		return active;
	}

	public boolean getActive() {
		return isActive();
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void addTransaction(AccountTransaction transaction) {
		//TODO: Check balance and available calculation
		//TODO: Write test for balance and available calculation
		switch (transaction.getTransactionType()) {
		case RESERVATION:
			this.available = available.add(transaction.getAmount());
			break;
		case LOST:
			this.balance = balance.add(transaction.getAmount());
			break;
		default:
			this.balance = balance.add(transaction.getAmount());
			this.available = available.add(transaction.getAmount());
		}
		this.transactions.add(transaction);
	}

	public void setDefaultAccount(boolean defaultAccount) {
		this.defaultAccount = defaultAccount;
	}

	public boolean isDefaultAccount() {
		return defaultAccount;
	}
	
	public boolean getDefaultAccount() {
		return defaultAccount;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", ownerId=" + ownerId + ", currency="
				+ currency + ", balance=" + balance + ", available="
				+ available + ", created=" + created + ", active=" + active
				+ ", defaultAccount=" + defaultAccount + "]";
	}
	
}
