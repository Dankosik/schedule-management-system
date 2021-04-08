package com.foxminded.university.management.schedule.models.validators;

import com.foxminded.university.management.schedule.models.validators.impl.GroupNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GroupNameValidator.class)
public @interface GroupName {
    String message() default "Must contain capital letters, only one dash and two digits, example: \"AB-01\"";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
