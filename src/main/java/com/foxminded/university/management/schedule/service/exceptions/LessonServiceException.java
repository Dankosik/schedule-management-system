package com.foxminded.university.management.schedule.service.exceptions;

public class LessonServiceException extends ServiceException {
    public LessonServiceException(Throwable cause) {
        super(cause);
    }

    public LessonServiceException(String message) {
        super(message);
    }
}
