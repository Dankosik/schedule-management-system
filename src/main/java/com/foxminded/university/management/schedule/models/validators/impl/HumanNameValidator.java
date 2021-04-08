package com.foxminded.university.management.schedule.models.validators.impl;

import com.foxminded.university.management.schedule.models.validators.HumanName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HumanNameValidator implements ConstraintValidator<HumanName, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return false;

        boolean firstLetterIsUpperCase = Character.isUpperCase(value.charAt(0));

        return value.chars().noneMatch(Character::isSpaceChar) &&
                value.chars().allMatch(Character::isLetter) &&
                value.substring(1).chars().allMatch(Character::isLowerCase) &&
                firstLetterIsUpperCase;
    }
}
