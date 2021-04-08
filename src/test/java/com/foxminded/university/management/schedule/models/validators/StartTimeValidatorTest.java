package com.foxminded.university.management.schedule.models.validators;

import com.foxminded.university.management.schedule.models.validators.impl.StartTimeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.sql.Time;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class StartTimeValidatorTest {
    private StartTimeValidator startTimeValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        startTimeValidator = new StartTimeValidator();
    }

    @Test
    void shouldReturnTrueOnValidInput() {
        assertTrue(startTimeValidator.isValid(Time.valueOf(LocalTime.of(14, 10, 0)), constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseOnNotValidInput() {
        assertFalse(startTimeValidator.isValid(Time.valueOf(LocalTime.of(23, 10, 0)), constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseOnNull() {
        assertFalse(startTimeValidator.isValid(null, constraintValidatorContext));
    }
}
