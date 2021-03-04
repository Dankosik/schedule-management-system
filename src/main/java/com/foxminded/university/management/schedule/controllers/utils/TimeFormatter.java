package com.foxminded.university.management.schedule.controllers.utils;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.Locale;

@Component
public class TimeFormatter implements Formatter<Time> {
    @Override
    public Time parse(String s, Locale locale) {
        if (s.length() <= 5) {
            s += ":00";
        }
        return Time.valueOf(s);
    }

    @Override
    public String print(Time time, Locale locale) {
        return time.toString();
    }
}
