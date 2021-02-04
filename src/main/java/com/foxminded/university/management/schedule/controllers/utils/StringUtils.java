package com.foxminded.university.management.schedule.controllers.utils;

import java.time.Duration;

public class StringUtils {
    public static String formatDuration(Duration duration) {
        long s = duration.getSeconds();
        return String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
    }
}
