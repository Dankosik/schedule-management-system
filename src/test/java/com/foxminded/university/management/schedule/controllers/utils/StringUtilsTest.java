package com.foxminded.university.management.schedule.controllers.utils;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {
    @Test
    public void shouldReturnFormattedDuration() {
        String expected = "1:30:00";
        String actual = StringUtils.formatDurationInMinutes(Duration.ofMinutes(90));
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnFormattedZeroDuration() {
        String expected = "0:00:00";
        String actual = StringUtils.formatDurationInMinutes(Duration.ofMinutes(0));
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnFormattedNullDuration() {
        String expected = null;
        String actual = StringUtils.formatDurationInMinutes(null);
        assertEquals(expected, actual);
    }
}
