package com.foxminded.university.management.schedule.models.validators;

import com.foxminded.university.management.schedule.models.validators.impl.SubjectNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SubjectNameValidator.class)
public @interface SubjectName {
    String message() default "Must be start with capital letter, Must not contain digits and spaces, other letters should be in lower case, example: \"Math\"";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
