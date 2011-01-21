package se.telescopesoftware.betpals.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * Helper class to keep all information needed to create quick competition.
 * Used only to pass user entered info from web page.
 *
 */
public class QuickCompetition {

	private String name;
	private String description;
	private BigDecimal stake;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	private Date deadline;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	private Date settlingDeadline;
	private boolean facebookPublish;
	private boolean allFriends;
	private Set<Long> friendsIdSet;
	private Set<Long> groupIdSet;
	private Long accountId;
	private MultipartFile imageFile;
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public BigDecimal getStake() {
		return stake;
	}
	
	public void setStake(BigDecimal stake) {
		this.stake = stake;
	}
	
	public Date getDeadline() {
		return deadline;
	}
	
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
	public boolean isFacebookPublish() {
		return facebookPublish;
	}
	
	public void setFacebookPublish(boolean facebookPublish) {
		this.facebookPublish = facebookPublish;
	}
	
	public boolean isAllFriends() {
		return allFriends;
	}
	
	public void setAllFriends(boolean allFriends) {
		this.allFriends = allFriends;
	}
	
	public Set<Long> getFriendsIdSet() {
		return friendsIdSet;
	}
	
	public void setFriendsIdSet(Set<Long> friendsIdSet) {
		this.friendsIdSet = friendsIdSet;
	}
	
	public Set<Long> getGroupIdSet() {
		return groupIdSet;
	}
	
	public void setGroupIdSet(Set<Long> groupIdSet) {
		this.groupIdSet = groupIdSet;
	}
	
	public MultipartFile getImageFile() {
		return imageFile;
	}
	
	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Competition createCompetition() {
    	Competition competition = new Competition(getName());
    	competition.setCompetitionType(CompetitionType.QUICK);
    	competition.setFixedStake(getStake());
    	competition.setAccessType(AccessType.PRIVATE);
    	competition.setDescription(getDescription());
    	
    	competition.setDeadline(getDeadline());
    	competition.setSettlingDeadline(getSettlingDeadline());

    	Event event = new Event(getName());
    	Alternative alternative = new Alternative("True");
    	alternative.setAlternativeType(AlternativeType.TRUE);
    	alternative.setTaken(true);
    	event.addAlternative(alternative);
    	alternative = new Alternative("False");
    	alternative.setAlternativeType(AlternativeType.FALSE);
    	event.addAlternative(alternative);

    	competition.addEvent(event);

    	return competition;
	}

	public Date getSettlingDeadline() {
		return settlingDeadline;
	}

	public void setSettlingDeadline(Date settlingDeadline) {
		this.settlingDeadline = settlingDeadline;
	}
	
}
