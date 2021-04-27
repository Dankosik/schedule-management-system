package com.foxminded.university.management.schedule.controllers.rest.exceptions;

import com.foxminded.university.management.schedule.exceptions.RestException;

public class UnacceptableUriException extends RestException {
    public UnacceptableUriException(Throwable cause) {
        super(cause);
    }

    public UnacceptableUriException(String message) {
        super(message);
    }
}
