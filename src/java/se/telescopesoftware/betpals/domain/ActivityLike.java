package se.telescopesoftware.betpals.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="activities_likes")
public class ActivityLike {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long ownerId;
	private String ownerName;
	private Date created;
	
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="activityId")
    private Activity activity;
	
	
	public ActivityLike() {
	}

	public ActivityLike(UserProfile userProfile, Activity activity) {
    	this.setCreated(new Date());
    	this.setOwnerId(userProfile.getUserId());
    	this.setOwnerName(userProfile.getFullName());
    	this.setActivity(activity);
	}
	
	public Long getId() {
		return id;
	}
	
	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}
	
	public Long getActivityId() {
		return activity.getId();
	}
	
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	public Long getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public boolean checkOwnership(Long userId) {
		return getOwnerId().compareTo(userId) == 0;
	}

	public String toString() {
		return "ActivityLike [id=" + id + ", activityId=" + getActivityId()
				+ ", ownerId=" + ownerId + ", ownerName=" + ownerName
				+ ", created=" + created + "]";
	}

}
