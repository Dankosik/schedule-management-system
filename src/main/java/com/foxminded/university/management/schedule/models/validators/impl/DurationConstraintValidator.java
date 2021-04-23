package com.foxminded.university.management.schedule.models.validators.impl;

import com.foxminded.university.management.schedule.models.validators.DurationConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;

public class DurationConstraintValidator implements ConstraintValidator<DurationConstraint, Duration> {
    @Override
    public boolean isValid(Duration value, ConstraintValidatorContext context) {
        if (value == null) return false;
        int lowBoard = 1800;
        int upperBoard = 7200;
        long durationInSeconds = value.getSeconds();
        return durationInSeconds >= lowBoard && durationInSeconds <= upperBoard;
    }
}
