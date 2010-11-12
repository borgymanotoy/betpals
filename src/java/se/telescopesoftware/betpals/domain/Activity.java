package se.telescopesoftware.betpals.domain;

import java.util.Collection;
import java.util.Date;

public class Activity {

	private Long id;
	private Long ownerId;
	private String ownerName;
	private ActivityType activityType;
	private String message;
	private Date created;
	
	private Collection<ActivityComment> comments;
	private Collection<ActivityLike> likes;
	
	
	public Long getId() {
		return id;
	}
	
	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}
	
	public Long getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	public ActivityType getActivityType() {
		return activityType;
	}
	
	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Collection<ActivityComment> getComments() {
		return comments;
	}

	public void setComments(Collection<ActivityComment> collection) {
		this.comments = collection;
	}

	public Collection<ActivityLike> getLikes() {
		return likes;
	}

	public void setLikes(Collection<ActivityLike> likes) {
		this.likes = likes;
	}
	
	
}
