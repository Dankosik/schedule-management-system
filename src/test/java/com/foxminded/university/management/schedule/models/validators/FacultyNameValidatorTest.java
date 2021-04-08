package com.foxminded.university.management.schedule.models.validators;

import com.foxminded.university.management.schedule.models.validators.impl.FacultyNameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class FacultyNameValidatorTest {
    private FacultyNameValidator facultyNameValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        facultyNameValidator = new FacultyNameValidator();
    }

    @Test
    void shouldReturnTrueOnValidInput() {
        assertTrue(facultyNameValidator.isValid("FAIT", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseOnInputDigits() {
        assertFalse(facultyNameValidator.isValid("231", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfAllLetterNotCapital() {
        assertFalse(facultyNameValidator.isValid("FaiT", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfFacultyNameContainsDigits() {
        assertFalse(facultyNameValidator.isValid("FAIT2", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseOnNull() {
        assertFalse(facultyNameValidator.isValid(null, constraintValidatorContext));
    }
}
