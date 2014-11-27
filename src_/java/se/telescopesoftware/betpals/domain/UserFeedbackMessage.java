package se.telescopesoftware.betpals.domain;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public class UserFeedbackMessage {

	
	private String reportId;
	private String pageUrl;
	private String userName;
	private String userEmail;
	private String comment;
	
	public UserFeedbackMessage() {
		DateTime now = new DateTime();
		DateTimeFormatter fmt = new DateTimeFormatterBuilder()
        .appendYear(4, 4)
        .appendMonthOfYear(1)
        .appendDayOfMonth(1)
        .appendLiteral('-')
        .appendSecondOfDay(1)
        .toFormatter();
		this.reportId = now.toString(fmt);
	}
	
	public UserFeedbackMessage(UserProfile userProfile) {
		this();
		this.userName = userProfile.getFullName();
		this.userEmail = userProfile.getEmail();
	}
	
	public String getReportId() {
		return reportId;
	}
	
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
	public String getPageUrl() {
		return pageUrl;
	}
	
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
}
