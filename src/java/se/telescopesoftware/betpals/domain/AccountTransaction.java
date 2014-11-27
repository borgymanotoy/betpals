package se.telescopesoftware.betpals.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a money transaction on user account.
 *
 */
@Entity
@Table(name="account_transaction")
public class AccountTransaction implements Serializable {

	private static final long serialVersionUID = 3617956803546707278L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long accountId;
    @Enumerated(EnumType.STRING)
	private AccountTransactionType transactionType;
	private Date transactionDate;
	private Long sourceId;
	private Long destinationId;
	private String currency;
	private BigDecimal amount;
	private String description;
	
	
	public AccountTransaction() {
	}
	
	public AccountTransaction(Account account, BigDecimal amount, AccountTransactionType type) {
		this.accountId = account.getId();
		this.amount = amount;
		this.currency = account.getCurrency();
		this.transactionType = type;
		switch (type) {
		case DEPOSIT:
		case WON:
			this.destinationId = account.getId();
			break;
		case WITHDRAW:
		case RESERVATION:
		case LOST:
			this.sourceId = account.getId();
			break;
		}
		this.transactionDate = new Date(); //TODO: Store dates in UTC
	}

	
	public Long getId() {
		return id;
	}
	
	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}
	
	public AccountTransactionType getTransactionType() {
		return transactionType;
	}
	
	public void setTransactionType(AccountTransactionType transactionType) {
		this.transactionType = transactionType;
	}
	
	public Date getTransactionDate() {
		return transactionDate;
	}
	
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public Long getSourceId() {
		return sourceId;
	}
	
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
	
	public Long getDestinationId() {
		return destinationId;
	}
	
	public void setDestinationId(Long destinationId) {
		this.destinationId = destinationId;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "AccountTransaction [id=" + id + ", accountId=" + accountId
				+ ", transactionType=" + transactionType + ", transactionDate="
				+ transactionDate + ", sourceId=" + sourceId
				+ ", destinationId=" + destinationId + ", currency=" + currency
				+ ", amount=" + amount + ", description=" + description + "]";
	}
	
}
