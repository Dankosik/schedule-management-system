package com.foxminded.university.management.schedule.models.validators;

import com.foxminded.university.management.schedule.models.validators.impl.HumanNameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HumanNameValidatorTest {
    private HumanNameValidator humanNameValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        humanNameValidator = new HumanNameValidator();
    }

    @Test
    void shouldReturnTrueOnValidInput() {
        assertTrue(humanNameValidator.isValid("John", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseOnInputDigits() {
        assertFalse(humanNameValidator.isValid("231", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfAllLetterAreCapital() {
        assertFalse(humanNameValidator.isValid("JOHN", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfHumanNameContainsDigits() {
        assertFalse(humanNameValidator.isValid("John2", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfHumanNameContainsSpaces() {
        assertFalse(humanNameValidator.isValid("Joh n", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfHumanNameContainsNotLetter() {
        assertFalse(humanNameValidator.isValid("Joh_n", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseOnNull() {
        assertFalse(humanNameValidator.isValid(null, constraintValidatorContext));
    }
}
