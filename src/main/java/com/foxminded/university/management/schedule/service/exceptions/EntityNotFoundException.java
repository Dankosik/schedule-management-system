package com.foxminded.university.management.schedule.service.exceptions;

import com.foxminded.university.management.schedule.exceptions.ServiceException;

public class EntityNotFoundException extends ServiceException {
    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
