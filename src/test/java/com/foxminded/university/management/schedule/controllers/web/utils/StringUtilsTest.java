package com.foxminded.university.management.schedule.controllers.web.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class StringUtilsTest {
    private final List<Duration> durations = List.of(Duration.ofMinutes(90), Duration.ofMinutes(90), Duration.ofMinutes(90), Duration.ofMinutes(80));
    @MockBean
    private DurationFormatter durationFormatter;

    @Test
    public void shouldReturnFormattedListOfDurations() {
        when(durationFormatter.print(Duration.ofMinutes(90), Locale.getDefault())).thenReturn("1:30");
        when(durationFormatter.print(Duration.ofMinutes(80), Locale.getDefault())).thenReturn("1:20");

        List<String> expected = List.of("1:30", "1:30", "1:30", "1:20");
        List<String> actual = StringUtils.formatListOfDurations(durations);

        assertEquals(expected, actual);
    }

}
