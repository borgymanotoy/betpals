package se.telescopesoftware.betpals.web.decorators;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.displaytag.decorator.TableDecorator;

import se.telescopesoftware.betpals.domain.UserProfile;

public class UserTableDecorator extends TableDecorator {
	
	private DateFormat dateFormat;
	
	public UserTableDecorator() {
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}	

	public String getRegistrationDate() {
		UserProfile userProfile = (UserProfile) getCurrentRowObject();
		return formatDate(userProfile.getRegistrationDate());
	}
	
	public String getLastLoginDate() {
		UserProfile userProfile = (UserProfile) getCurrentRowObject();
		return formatDate(userProfile.getLastLoginDate());
	}
	
	public String getLastBetDate() {
		UserProfile userProfile = (UserProfile) getCurrentRowObject();
		return formatDate(userProfile.getLastBetDate());
	}
	
	public String getLastCompetitionDate() {
		UserProfile userProfile = (UserProfile) getCurrentRowObject();
		return formatDate(userProfile.getLastCompetitionDate());
	}
	
	private String formatDate(Date date) {
		return date != null ? this.dateFormat.format(date) : "";
	}
	
}
