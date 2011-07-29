package se.telescopesoftware.betpals.web.decorators;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.displaytag.decorator.TableDecorator;

import se.telescopesoftware.betpals.domain.Bet;

public class BetTableDecorator extends TableDecorator {
	
	private DateFormat dateFormat;
	private DecimalFormat decimalFormat;
	
	public BetTableDecorator() {
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		this.decimalFormat = new DecimalFormat("0.00");
	}	

	public String getPlaced() {
		Bet bet = (Bet) getCurrentRowObject();
		return formatDate(bet.getPlaced());
	}
	
	public String getSettled() {
		Bet bet = (Bet) getCurrentRowObject();
		return formatDate(bet.getSettled());
	}
	
	public String getStake() {
		Bet bet = (Bet) getCurrentRowObject();
		return decimalFormat.format(bet.getStake());
	}
	
	public String getProfitOrLoss() {
		Bet bet = (Bet) getCurrentRowObject();
		return decimalFormat.format(bet.getProfitOrLoss());
	}
	
	private String formatDate(Date date) {
		return date != null ? this.dateFormat.format(date) : "";
	}
	
}
