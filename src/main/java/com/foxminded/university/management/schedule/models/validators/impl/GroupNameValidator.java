package com.foxminded.university.management.schedule.models.validators.impl;

import com.foxminded.university.management.schedule.models.validators.GroupName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GroupNameValidator implements ConstraintValidator<GroupName, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;

        if (value.length() < 3) return false;

        boolean isContainsSpaces = value.chars().anyMatch(Character::isSpaceChar);
        if (isContainsSpaces) return false;

        boolean isContainsDash = value.chars().anyMatch(c -> c == '-');
        if (!isContainsDash) return false;

        char[] chars = value.toCharArray();
        int indexOfDash = getIndexOfDash(chars);

        if (chars.length - indexOfDash > 3) return false;

        for (int i = 0; i < indexOfDash; i++) {
            if (Character.isDigit(chars[i]) || Character.isLowerCase(chars[i])) return false;
        }

        for (int i = indexOfDash + 1; i < chars.length; i++) {
            if (!Character.isDigit(chars[i])) return false;
        }

        return true;
    }

    private int getIndexOfDash(char[] chars) {
        int indexOfDash = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '-') {
                indexOfDash = i;
                break;
            }
        }
        return indexOfDash;
    }
}
