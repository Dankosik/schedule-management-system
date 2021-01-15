package com.foxminded.university.management.schedule.service.exceptions;

public class StudentServiceException extends ServiceException {
    public StudentServiceException(Throwable cause) {
        super(cause);
    }

    public StudentServiceException(String message) {
        super(message);
    }
}
