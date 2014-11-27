package se.telescopesoftware.betpals.web.decorators;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.displaytag.decorator.TableDecorator;

import se.telescopesoftware.betpals.domain.Competition;

public class CompetitionTableDecorator extends TableDecorator {
	
	private DateFormat dateFormat;
	
	public CompetitionTableDecorator() {
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}	

	public String getDeadline() {
		Competition competition = (Competition) getCurrentRowObject();
		return formatDate(competition.getDeadline());
	}
	
	public String getSettlingDeadline() {
		Competition competition = (Competition) getCurrentRowObject();
		return formatDate(competition.getSettlingDeadline());
	}
	
	public String getOwner() {
		Competition competition = (Competition) getCurrentRowObject();
		return competition.getOwner().getFullName();
	}
	
	private String formatDate(Date date) {
		return date != null ? this.dateFormat.format(date) : "";
	}
	
}
