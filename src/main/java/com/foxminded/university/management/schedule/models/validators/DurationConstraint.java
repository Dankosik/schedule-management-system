package com.foxminded.university.management.schedule.models.validators;

import com.foxminded.university.management.schedule.models.validators.impl.DurationConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DurationConstraintValidator.class)
public @interface DurationConstraint {
    String message() default "Duration should be between 30 minutes and 2 hours";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
