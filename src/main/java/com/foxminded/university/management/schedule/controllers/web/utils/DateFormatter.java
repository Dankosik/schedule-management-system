package com.foxminded.university.management.schedule.controllers.web.utils;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Locale;

@Component
public class DateFormatter implements Formatter<Date> {
    @Override
    public Date parse(String s, Locale locale) {
        if (s.isEmpty()) return new Date(0);
        return Date.valueOf(s);
    }

    @Override
    public String print(Date date, Locale locale) {
        return date.toString();
    }
}
