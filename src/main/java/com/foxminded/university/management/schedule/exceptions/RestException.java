package com.foxminded.university.management.schedule.exceptions;

public class RestException extends RuntimeException {
    public RestException(Throwable cause) {
        super(cause);
    }

    public RestException(String message) {
        super(message);
    }
}
