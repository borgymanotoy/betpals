package se.telescopesoftware.betpals.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="bet")
public class Bet {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long ownerId;
	private Long accountId;
	private Long selectionId;
	private String currency;
	private BigDecimal odds;
	private BigDecimal stake;
	private String details;
	private Date placed;
	private Date settled;
	private BigDecimal profitOrLoss;
	private String ownerName;
	
	
	public Long getId() {
		return id;
	}
	
	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}
	
	public Long getSelectionId() {
		return selectionId;
	}
	
	public void setSelectionId(Long selectionId) {
		this.selectionId = selectionId;
	}
	
	public BigDecimal getOdds() {
		return odds;
	}
	
	public void setOdds(BigDecimal odds) {
		this.odds = odds;
	}
	
	public BigDecimal getStake() {
		return stake;
	}
	
	public void setStake(BigDecimal stake) {
		this.stake = stake;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Date getPlaced() {
		return placed;
	}

	public void setPlaced(Date placed) {
		this.placed = placed;
	}

	public Date getSettled() {
		return settled;
	}

	public void setSettled(Date settled) {
		this.settled = settled;
	}

	public BigDecimal getProfitOrLoss() {
		return profitOrLoss;
	}

	public void setProfitOrLoss(BigDecimal profitOrLoss) {
		this.profitOrLoss = profitOrLoss;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	
}
