package com.foxminded.university.management.schedule.controllers.rest.utils;

import com.foxminded.university.management.schedule.controllers.rest.errors.ErrorRequestBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RestUtils {
    public static ResponseEntity<Object> buildErrorResponseEntity(String message, HttpStatus httpStatus) {
        ErrorRequestBuilder errorRequestBuilder = new ErrorRequestBuilder(new Date(), httpStatus, message, httpStatus.value());
        return new ResponseEntity<>(errorRequestBuilder, errorRequestBuilder.getError());
    }
}
