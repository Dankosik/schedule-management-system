package com.foxminded.university.management.schedule.controllers.rest.exceptions;

import com.foxminded.university.management.schedule.exceptions.RestException;

public class UnacceptableLectureNumberException extends RestException {
    public UnacceptableLectureNumberException(Throwable cause) {
        super(cause);
    }

    public UnacceptableLectureNumberException(String message) {
        super(message);
    }
}
