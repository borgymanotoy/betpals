package se.telescopesoftware.betpals.validation;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import se.telescopesoftware.betpals.domain.QuickCompetition;

public class QuickCompetitionValidator implements ConstraintValidator<QuickCompetitionConstraints, QuickCompetition> {

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
		
		return true;
	}

}
