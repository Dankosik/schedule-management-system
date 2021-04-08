package com.foxminded.university.management.schedule.models.validators;

import com.foxminded.university.management.schedule.models.validators.impl.StartTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartTimeValidator.class)
public @interface StartTime {
    String message() default "Start time should be between 8:30 and 21:00";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
