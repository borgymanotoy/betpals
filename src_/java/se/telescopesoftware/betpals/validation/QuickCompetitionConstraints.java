package se.telescopesoftware.betpals.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Target({ TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = QuickCompetitionValidator.class)
@Documented
public @interface QuickCompetitionConstraints {

	String message() default "{quick.competition.constraint}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
