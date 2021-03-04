package com.foxminded.university.management.schedule.controllers.utils;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Locale;

@Component
public class DurationFormatter implements Formatter<Duration> {
    @Override
    public Duration parse(String s, Locale locale) {
        return Duration.parse(s);
    }

    @Override
    public String print(Duration duration, Locale locale) {
        if (duration == null) return null;
        long seconds = duration.getSeconds();
        return String.format("%d:%02d", seconds / 3600, (seconds % 3600) / 60);
    }
}
