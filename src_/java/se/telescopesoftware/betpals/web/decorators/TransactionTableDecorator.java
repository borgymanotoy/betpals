package se.telescopesoftware.betpals.web.decorators;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.displaytag.decorator.TableDecorator;

import se.telescopesoftware.betpals.domain.AccountTransaction;

public class TransactionTableDecorator extends TableDecorator {
	
	private DateFormat dateFormat;
	private DecimalFormat decimalFormat;
	
	public TransactionTableDecorator() {
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		this.decimalFormat = new DecimalFormat("0.00");
	}	

	public String getTransactionDate() {
		AccountTransaction transaction = (AccountTransaction) getCurrentRowObject();
		return formatDate(transaction.getTransactionDate());
	}
	
	public String getAmount() {
		AccountTransaction transaction = (AccountTransaction) getCurrentRowObject();
		return decimalFormat.format(transaction.getAmount());
	}
	
	private String formatDate(Date date) {
		return date != null ? this.dateFormat.format(date) : "";
	}
	
}
