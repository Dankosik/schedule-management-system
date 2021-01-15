package com.foxminded.university.management.schedule.service.exceptions;

public class GroupServiceException extends ServiceException {
    public GroupServiceException(Throwable cause) {
        super(cause);
    }

    public GroupServiceException(String message) {
        super(message);
    }
}
