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

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

@Entity
@Table(name="activities_comments")
public class ActivityComment {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long ownerId;
	private String ownerName;
	private String message;
	private Date created;
	
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="activityId")
    private Activity activity;
	
	public ActivityComment() {
	}

	public ActivityComment(UserProfile userProfile, String message, Activity activity) {
    	this.setCreated(new Date());
    	this.setOwnerId(userProfile.getUserId());
    	this.setOwnerName(userProfile.getFullName());
    	this.setMessage(message);
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
	
	public Activity getActivity() {
		return activity;
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
	
	public boolean checkOwnership(Long userId) {
		return getOwnerId().compareTo(userId) == 0;
	}
	
	public String getTimeSinceCreated() {
		Period period = new Period(new DateTime(getCreated()), new DateTime());
		PeriodFormatter formatter = new PeriodFormatterBuilder()
	     .appendYears()
	     .appendSuffix(" year", " years")
	     .appendSeparator(" ")
	     .appendMonths()
	     .appendSuffix(" month", " months")
	     .appendSeparator(" ")
	     .appendDays()
	     .appendSuffix(" day", " days")
	     .appendSeparator(" ")
	     .appendHours()
	     .appendSuffix(" hour", " hours")
	     .appendSeparator(" ")
	     .printZeroAlways()
	     .appendMinutes()
	     .appendSuffix(" minute", " minutes")
	     .appendSuffix(" ago")
	     .toFormatter();

		return formatter.print(period);
	}
	
	

	public String toString() {
		return "ActivityComment [id=" + id + ", activityId=" + getActivityId()
				+ ", ownerId=" + ownerId + ", ownerName=" + ownerName
				+ ", message=" + message + ", created=" + created + "]";
	}
	
}
