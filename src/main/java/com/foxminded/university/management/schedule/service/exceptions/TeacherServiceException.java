package com.foxminded.university.management.schedule.service.exceptions;

public class TeacherServiceException extends ServiceException {
    public TeacherServiceException(Throwable cause) {
        super(cause);
    }

    public TeacherServiceException(String message) {
        super(message);
    }
}
