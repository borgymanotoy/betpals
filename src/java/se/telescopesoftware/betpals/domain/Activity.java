package se.telescopesoftware.betpals.domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

@Entity
@Table(name="activities")
public class Activity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long ownerId;
	private String ownerName;
	@Enumerated(EnumType.STRING)
	private ActivityType activityType;
	private String message;
	private Date created;
	
    @OneToMany(mappedBy="activity", fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Cascade(org.hibernate.annotations.CascadeType.REPLICATE)
    @Fetch(FetchMode.SUBSELECT)
	private Collection<ActivityComment> comments;
    @OneToMany(mappedBy="activity", fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Cascade(org.hibernate.annotations.CascadeType.REPLICATE)
    @Fetch(FetchMode.SUBSELECT)
	private Collection<ActivityLike> likes;
	
	
	public Activity() {
	}
	
	public Activity(UserProfile userProfile, ActivityType type) {
    	this.setCreated(new Date());
    	this.setOwnerId(userProfile.getUserId());
    	this.setOwnerName(userProfile.getFullName());
    	this.setActivityType(type);
	}
	
	public Activity(UserProfile userProfile, String message, ActivityType type) {
		this(userProfile, type);
		this.message = message;
	}
	
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

	public void addComment(ActivityComment comment) {
		this.comments.add(comment);
	}
	
	public Collection<ActivityLike> getLikes() {
		return likes;
	}

	public void setLikes(Collection<ActivityLike> likes) {
		this.likes = likes;
	}
	
	public void addLike(ActivityLike like) {
		this.likes.add(like);
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
	
	public boolean checkOwnership(Long userId) {
		return getOwnerId().compareTo(userId) == 0;
	}
	
	public boolean hasComments() {
		return !this.comments.isEmpty();
	}
	
	public boolean hasLikes() {
		return !this.likes.isEmpty();
	}
	
}
