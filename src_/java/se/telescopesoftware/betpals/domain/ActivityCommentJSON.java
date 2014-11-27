package se.telescopesoftware.betpals.domain;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class ActivityCommentJSON {

	private Long id;
	private Long ownerId;
	private Long activityId;
	private String ownerName;
	private String message;
	private Date created;
	
	
	public ActivityCommentJSON() {
	}

	public ActivityCommentJSON(UserProfile userProfile, String message, Activity activity) {
    	this.setCreated(new Date());
    	this.setOwnerId(userProfile.getUserId());
    	this.setOwnerName(userProfile.getFullName());
    	this.setMessage(message);
    	this.setActivityId(activity.getId());
	}
	
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	
	public Long getActivityId() {
		return activityId;
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
	
	

}
