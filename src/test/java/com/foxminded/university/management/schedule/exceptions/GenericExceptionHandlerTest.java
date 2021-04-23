package com.foxminded.university.management.schedule.exceptions;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.foxminded.university.management.schedule.controllers.rest.errors.ErrorRequestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {GenericExceptionHandler.class})
class GenericExceptionHandlerTest {
    @Autowired
    private GenericExceptionHandler genericExceptionHandler;
    @Mock
    private JsonParser jsonParser;

    @Mock
    private JsonLocation jsonLocation;

    @Test
    void handleUnrecognizedPropertyException() {
        UnrecognizedPropertyException unrecognizedPropertyException = new UnrecognizedPropertyException(jsonParser, null, jsonLocation, null, "name", null);
        ResponseEntity<Object> actual = genericExceptionHandler.handleUnrecognizedPropertyException(unrecognizedPropertyException);
        ResponseEntity<Object> expected = new ResponseEntity<>(new ErrorRequestBuilder(new Date(), HttpStatus.BAD_REQUEST,
                "JSON parse error: Unrecognized field [name]", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
        assertEquals(expected.getStatusCodeValue(), actual.getStatusCodeValue());
        assertEquals(expected.getHeaders(), actual.getHeaders());
        assertEquals(expected.getBody().toString(), actual.getBody().toString());
    }

    @Test
    void handleUnrecognizedPropertyExceptionEmptyString() {
        UnrecognizedPropertyException unrecognizedPropertyException = new UnrecognizedPropertyException(jsonParser, null, jsonLocation, null, "", null);
        ResponseEntity<Object> actual = genericExceptionHandler.handleUnrecognizedPropertyException(unrecognizedPropertyException);
        ResponseEntity<Object> expected = new ResponseEntity<>(new ErrorRequestBuilder(new Date(), HttpStatus.BAD_REQUEST,
                "JSON parse error: Unrecognized field []", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
        assertEquals(expected.getStatusCodeValue(), actual.getStatusCodeValue());
        assertEquals(expected.getHeaders(), actual.getHeaders());
        assertEquals(expected.getBody().toString(), actual.getBody().toString());
    }
}
