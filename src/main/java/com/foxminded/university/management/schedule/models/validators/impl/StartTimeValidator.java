package com.foxminded.university.management.schedule.models.validators.impl;

import com.foxminded.university.management.schedule.models.validators.StartTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StartTimeValidator implements ConstraintValidator<StartTime, Time> {
    @Override
    public boolean isValid(Time time, ConstraintValidatorContext constraintValidatorContext) {
        if (time == null) return false;

        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime startTime = LocalTime.parse("08:29:59", format);
        LocalTime endTime = LocalTime.parse("21:00:00", format);
        LocalTime targetTime = LocalTime.parse(time.toString(), format);

        return targetTime.isBefore(endTime) && targetTime.isAfter(startTime);
    }
}
