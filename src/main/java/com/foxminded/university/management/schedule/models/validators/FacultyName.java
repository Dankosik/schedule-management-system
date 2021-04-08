package com.foxminded.university.management.schedule.models.validators;

import com.foxminded.university.management.schedule.models.validators.impl.FacultyNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FacultyNameValidator.class)
public @interface FacultyName {
    String message() default "Must not contain digits and spaces, all letters must be capital" +
            "\n example: \"FAIT\"";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
