package com.foxminded.university.management.schedule.models.validators;

import com.foxminded.university.management.schedule.models.validators.impl.SubjectNameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SubjectNameValidatorTest {
    private SubjectNameValidator subjectNameValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        subjectNameValidator = new SubjectNameValidator();
    }

    @Test
    void shouldReturnTrueOnValidInput() {
        assertTrue(subjectNameValidator.isValid("Math", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseOnInputDigits() {
        assertFalse(subjectNameValidator.isValid("231", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfAllLetterAreCapital() {
        assertFalse(subjectNameValidator.isValid("MATH", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfHumanNameContainsDigits() {
        assertFalse(subjectNameValidator.isValid("Math2", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfHumanNameContainsSpaces() {
        assertFalse(subjectNameValidator.isValid("Mat h", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfHumanNameContainsNotLetter() {
        assertFalse(subjectNameValidator.isValid("Mat_h", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseOnNull() {
        assertFalse(subjectNameValidator.isValid(null, constraintValidatorContext));
    }
}
