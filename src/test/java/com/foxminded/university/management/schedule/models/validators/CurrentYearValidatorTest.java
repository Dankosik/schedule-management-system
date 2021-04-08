package com.foxminded.university.management.schedule.models.validators;

import com.foxminded.university.management.schedule.models.validators.impl.CurrentYearValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CurrentYearValidatorTest {
    private CurrentYearValidator currentYearValidator;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        currentYearValidator = new CurrentYearValidator();
    }

    @Test
    void shouldReturnTrueOnValidInput() {
        assertTrue(currentYearValidator.isValid(Date.valueOf(LocalDate.now()), constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseOnNotCurrentYear() {
        assertFalse(currentYearValidator.isValid(Date.valueOf(LocalDate.of(1, 1, 1)), constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseOnNull() {
        assertFalse(currentYearValidator.isValid(null, constraintValidatorContext));
    }
}
