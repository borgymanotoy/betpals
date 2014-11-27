package se.telescopesoftware.betpals.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="competition_log")
public class CompetitionLogEntry {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long competitionId;
	private Date created;
	private String message;
	
	
	private static final int SHORT_MESSAGE_LENGTH = 80; 

	
	public CompetitionLogEntry() {
		super();
		this.created = new Date();
	}

	public CompetitionLogEntry(Long competitionId, String message) {
		this();
		this.competitionId = competitionId;
		this.message = message;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getCompetitionId() {
		return competitionId;
	}
	
	public void setCompetitionId(Long competitionId) {
		this.competitionId = competitionId;
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
