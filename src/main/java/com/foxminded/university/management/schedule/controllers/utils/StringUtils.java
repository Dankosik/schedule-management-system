package com.foxminded.university.management.schedule.controllers.utils;

import java.time.Duration;

public class StringUtils {
    public static String formatDurationInMinutes(Duration duration) {
        long seconds = duration.getSeconds();
        return String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
    }
}
