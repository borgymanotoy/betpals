package se.telescopesoftware.betpals.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_log")
public class UserLogEntry {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long userId;
	private Date created;
	private String message;
	
	private static final int SHORT_MESSAGE_LENGTH = 80; 

	
	public UserLogEntry() {
		super();
		this.created = new Date();
	}

	public UserLogEntry(Long userId, String message) {
		this();
		this.userId = userId;
		this.message = message;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getShortMessage() {
		return message.length() > SHORT_MESSAGE_LENGTH ? message.substring(0, SHORT_MESSAGE_LENGTH) : message;
	}
	
}
