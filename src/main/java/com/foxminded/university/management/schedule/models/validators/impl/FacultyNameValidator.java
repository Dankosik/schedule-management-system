package com.foxminded.university.management.schedule.models.validators.impl;

import com.foxminded.university.management.schedule.models.validators.FacultyName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FacultyNameValidator implements ConstraintValidator<FacultyName, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return false;
        return value.chars().noneMatch(Character::isSpaceChar) &&
                value.chars().noneMatch(Character::isLowerCase) &&
                value.chars().allMatch(Character::isLetter);
    }
}
