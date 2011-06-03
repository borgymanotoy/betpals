package se.telescopesoftware.betpals.domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
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
	private Long extensionId;
	private String extensionName;
	
    @OneToMany(mappedBy="activity")
    @Cascade(CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
	private Collection<ActivityComment> comments;
    @OneToMany(mappedBy="activity")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(CascadeType.ALL)
	private Collection<ActivityLike> likes;
	
	
	public Activity() {
		this.setCreated(new Date());
	}
	
	public Activity(Long ownerId, String ownerName, String message, ActivityType activityType) {
		this();
		this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.message = message;
		this.activityType = activityType;
	}
	
	public Activity(UserProfile userProfile, ActivityType type) {
    	this(userProfile.getUserId(), userProfile.getFullName(), null, type);
	}
	
	public Activity(UserProfile userProfile, String message, ActivityType type) {
		this(userProfile.getUserId(), userProfile.getFullName(), message, type);
	}
	
	public Activity(UserProfile userProfile, Long extensionId, String extensionName, String message, ActivityType type) {
		this(userProfile.getUserId(), userProfile.getFullName(), message, type);
		this.extensionId = extensionId;
		this.extensionName = extensionName;
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

	public Long getExtensionId() {
		return extensionId;
	}

	public void setExtensionId(Long extensionId) {
		this.extensionId = extensionId;
	}

	public String getExtensionName() {
		return extensionName;
	}

	public void setExtensionName(String extensionName) {
		this.extensionName = extensionName;
	}

	@Override
	public String toString() {
		return "Activity [id=" + id + ", ownerId=" + ownerId + ", ownerName="
				+ ownerName + ", activityType=" + activityType + ", created="
				+ created + ", extensionId=" + extensionId + ", extensionName="
				+ extensionName + "]";
	}
	
}
