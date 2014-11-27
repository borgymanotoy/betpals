package se.telescopesoftware.betpals.validation;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import se.telescopesoftware.betpals.domain.Account;
import se.telescopesoftware.betpals.domain.QuickCompetition;
import se.telescopesoftware.betpals.services.AccountService;

public class QuickCompetitionValidator implements ConstraintValidator<QuickCompetitionConstraints, QuickCompetition> {

	private AccountService accountService;

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public void initialize(QuickCompetitionConstraints arg0) {
	}

	public boolean isValid(QuickCompetition quickCompetition, ConstraintValidatorContext validatorContext) {
		Date deadlineDate = quickCompetition.getDeadline();
		Date settlingDate = quickCompetition.getSettlingDeadline();
		
		// Settling date should be after deadline date
		if (deadlineDate != null && settlingDate != null && deadlineDate.after(settlingDate)) {
			validatorContext.disableDefaultConstraintViolation();
			validatorContext.buildConstraintViolationWithTemplate("{quick.competition.constraint.deadline.date}").addNode("deadline").addConstraintViolation();
			return false;
		}
		
		Account account = accountService.getAccount(quickCompetition.getAccountId());
		if (!account.isValidStake(quickCompetition.getStake())) {
			validatorContext.disableDefaultConstraintViolation();
			validatorContext.buildConstraintViolationWithTemplate("{quick.competition.constraint.stake}").addNode("stake").addConstraintViolation();
			return false;
		}
		
		return true;
	}

}
