package com.foxminded.university.management.schedule.controllers.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = DurationFormatter.class)
class DurationFormatterTest {
    @Autowired
    private DurationFormatter durationFormatter;

    @Test
    public void shouldReturnDurationOnInputString() {
        Duration expected = Duration.ofMinutes(90);
        Duration actual = durationFormatter.parse("01:30:00", Locale.getDefault());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnDurationOnInputStringDuration() {
        Duration expected = Duration.ofMinutes(90);
        Duration actual = durationFormatter.parse("PT1H30M", Locale.getDefault());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnFormattedStringOnInputDuration() {
        String expected = "1:30";
        String actual = durationFormatter.print(Duration.ofMinutes(90), Locale.getDefault());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnFormattedZeroDuration() {
        String expected = "0:00";
        String actual = durationFormatter.print(Duration.ofMinutes(0), Locale.getDefault());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnFormattedNullDuration() {
        String expected = null;
        String actual = durationFormatter.print(null, Locale.getDefault());
        assertEquals(expected, actual);
    }
}
