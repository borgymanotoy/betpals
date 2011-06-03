package se.telescopesoftware.betpals.validation;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import se.telescopesoftware.betpals.domain.Competition;
import se.telescopesoftware.betpals.domain.CompetitionType;

public class CompetitionValidator implements ConstraintValidator<CompetitionConstraints, Competition> {

	public void initialize(CompetitionConstraints arg0) {
	}

	public boolean isValid(Competition competition, ConstraintValidatorContext validatorContext) {
		Date deadlineDate = competition.getDeadline();
		Date settlingDate = competition.getSettlingDeadline();
		
		// Settling date should be after deadline date
		if (deadlineDate != null && settlingDate != null && deadlineDate.after(settlingDate)) {
			validatorContext.disableDefaultConstraintViolation();
			validatorContext.buildConstraintViolationWithTemplate("{competition.constraint.deadline.date}").addNode("deadline").addConstraintViolation();
			return false;
		} else if (competition.getCompetitionType() == CompetitionType.FIXED_STAKE && competition.getFixedStake() == null) {
			validatorContext.disableDefaultConstraintViolation();
			validatorContext.buildConstraintViolationWithTemplate("{competition.constraint.fixed.stake}").addNode("fixedStake").addConstraintViolation();
			return false;
		}
		
		return true;
	}

}
