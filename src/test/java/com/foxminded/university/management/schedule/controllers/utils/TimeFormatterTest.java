package com.foxminded.university.management.schedule.controllers.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;
import java.text.ParseException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TimeFormatter.class)
class TimeFormatterTest {
    @Autowired
    private TimeFormatter timeFormatter;

    @Test
    public void shouldReturnTimeOnInputString() throws ParseException {
        Time expected = Time.valueOf("01:30:00");
        Time actual = timeFormatter.parse("1:30", Locale.getDefault());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnFormattedStringOnInputTime() throws ParseException {
        String expected = "01:30:00";
        String actual = timeFormatter.print(Time.valueOf("01:30:00"), Locale.getDefault());
        assertEquals(expected, actual);
    }
}
