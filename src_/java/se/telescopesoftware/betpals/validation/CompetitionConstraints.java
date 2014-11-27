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
@Constraint(validatedBy = CompetitionValidator.class)
@Documented
public @interface CompetitionConstraints {

	String message() default "{competition.constraint}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
