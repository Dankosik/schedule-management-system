package com.foxminded.university.management.schedule.service.exceptions;

import com.foxminded.university.management.schedule.exceptions.ServiceException;

public class UniqueConstraintException extends ServiceException {
    public UniqueConstraintException(Throwable cause) {
        super(cause);
    }

    public UniqueConstraintException(String message) {
        super(message);
    }
}
