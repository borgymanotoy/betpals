package se.telescopesoftware.betpals.domain;

import java.util.Date;


public class LogEntry {

	private String message;
	private String additionalInfo;
	private LogEntryLevel level;
	private String source;
	private Date date;
	
	private int SHORT_MESSAGE_LENGTH = 80; 

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public LogEntryLevel getLevel() {
		return level;
	}

	public void setLevel(LogEntryLevel level) {
		this.level = level;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getShortMessage() {
		return message.length() > SHORT_MESSAGE_LENGTH ? message.substring(0, SHORT_MESSAGE_LENGTH) : message;
	}
	
}
