package com.foxminded.university.management.schedule.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.foxminded.university.management.schedule.controllers.rest.errors.ErrorRequestBuilder;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import com.foxminded.university.management.schedule.service.exceptions.UniqueConstraintException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GenericExceptionHandler {
    @ExceptionHandler(value = {UnrecognizedPropertyException.class})
    protected ResponseEntity<Object> handleUnrecognizedPropertyException(UnrecognizedPropertyException ex) {
        final String message = "JSON parse error: Unrecognized field " + "[" + ex.getPropertyName() + "]";

        ErrorRequestBuilder errorRequestBuilder =
                new ErrorRequestBuilder(new Date(), HttpStatus.BAD_REQUEST, message, HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errorRequestBuilder, errorRequestBuilder.getError());
    }

    @ExceptionHandler(value = {MismatchedInputException.class})
    protected ResponseEntity<Object> handleMismatchedInputException(MismatchedInputException ex) {
        String fieldErrorPath = getFieldErrorPath(ex);
        String exceptionMessage = ex.getOriginalMessage();
        if (exceptionMessage.startsWith("Cannot deserialize value of type")) {
            ErrorRequestBuilder errorRequestBuilder = new ErrorRequestBuilder(new Date(), HttpStatus.BAD_REQUEST,
                    exceptionMessage + " in: " + fieldErrorPath, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorRequestBuilder, errorRequestBuilder.getError());
        }

        final String message = "JSON parse error: Required field: [" + fieldErrorPath + "]";

        ErrorRequestBuilder errorRequestBuilder =
                new ErrorRequestBuilder(new Date(), HttpStatus.BAD_REQUEST, message, HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errorRequestBuilder, errorRequestBuilder.getError());
    }

    private String getFieldErrorPath(MismatchedInputException ex) {
        List<String> requiredFields = ex.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < requiredFields.size(); i++) {
            stringBuilder.append(requiredFields.get(i));
            if (!(i == requiredFields.size() - 1)) stringBuilder.append("->");
        }
        return stringBuilder.toString();
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorRequestBuilder errorRequestBuilder =
                new ErrorRequestBuilder(new Date(), HttpStatus.NOT_FOUND, ex.getMessage(), HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errorRequestBuilder, errorRequestBuilder.getError());
    }

    @ExceptionHandler(value = {UniqueConstraintException.class})
    protected ResponseEntity<Object> handleUniqueConstraintException(UniqueConstraintException ex) {
        ErrorRequestBuilder errorRequestBuilder =
                new ErrorRequestBuilder(new Date(), HttpStatus.CONFLICT, ex.getMessage(), HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(errorRequestBuilder, errorRequestBuilder.getError());
    }
}
