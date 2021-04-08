package com.foxminded.university.management.schedule.models.validators.impl;

import com.foxminded.university.management.schedule.models.validators.CurrentYear;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Date;

public class CurrentYearValidator implements ConstraintValidator<CurrentYear, Date> {
    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (value == null) return false;
        LocalDate currentDate = LocalDate.now();
        LocalDate dateOfValue = LocalDate.parse(value.toString());
        return currentDate.getYear() == dateOfValue.getYear();
    }
}
