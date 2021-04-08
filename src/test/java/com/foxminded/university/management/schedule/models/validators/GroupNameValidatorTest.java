package com.foxminded.university.management.schedule.models.validators;

import com.foxminded.university.management.schedule.models.validators.impl.GroupNameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

class GroupNameValidatorTest {
    private GroupNameValidator groupNameValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        groupNameValidator = new GroupNameValidator();
    }

    @Test
    void shouldReturnTrueOnValidInput() {
        assertTrue(groupNameValidator.isValid("SA-12", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseOnInputDigits() {
        assertFalse(groupNameValidator.isValid("231", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfAllLetterNotCapital() {
        assertFalse(groupNameValidator.isValid("Ada-11", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfGroupNameContainsDigitsInLeftPart() {
        assertFalse(groupNameValidator.isValid("12-ADA", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfGroupNameContainsLetterInRightPart() {
        assertFalse(groupNameValidator.isValid("AML-AQ", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfGroupNameContainsTwoDashes() {
        assertFalse(groupNameValidator.isValid("AML--AQ", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfGroupNameContainsMoreThan2Digits() {
        assertFalse(groupNameValidator.isValid("AML-213", constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseOnNull() {
        assertFalse(groupNameValidator.isValid(null, constraintValidatorContext));
    }
}
