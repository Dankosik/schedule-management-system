package com.foxminded.university.management.schedule.controllers.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = DateFormatter.class)
class DateFormatterTest {
    @Autowired
    private DateFormatter dateFormatter;

    @Test
    public void shouldReturnDateOnInputString() {
        Date expected = Date.valueOf("2021-03-04");
        Date actual = dateFormatter.parse("2021-03-04", Locale.getDefault());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnDateWithZeroOnEmptyString() {
        Date expected = new Date(0);
        Date actual = dateFormatter.parse("", Locale.getDefault());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnFormattedStringOnInputDate() {
        String expected = "2021-03-04";
        String actual = dateFormatter.print(Date.valueOf("2021-03-04"), Locale.getDefault());
        assertEquals(expected, actual);
    }
}
