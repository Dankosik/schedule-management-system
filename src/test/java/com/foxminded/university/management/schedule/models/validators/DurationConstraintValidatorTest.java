package com.foxminded.university.management.schedule.models.validators;

import com.foxminded.university.management.schedule.models.validators.impl.DurationConstraintValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DurationConstraintValidatorTest {
    private DurationConstraintValidator durationConstraintValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        durationConstraintValidator = new DurationConstraintValidator();
    }

    @Test
    void shouldReturnTrueOnValidInput() {
        assertTrue(durationConstraintValidator.isValid(Duration.ofMinutes(90), constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfInputDurationMoreThanUpperBoard() {
        assertFalse(durationConstraintValidator.isValid(Duration.ofMinutes(400), constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfInputDurationLessThanLowBoard() {
        assertFalse(durationConstraintValidator.isValid(Duration.ofMinutes(20), constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseOnNull() {
        assertFalse(durationConstraintValidator.isValid(null, constraintValidatorContext));
    }
}
