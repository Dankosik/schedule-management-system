package com.foxminded.university.management.schedule.service.exceptions;

public class LectureServiceException extends ServiceException {
    public LectureServiceException(Throwable cause) {
        super(cause);
    }

    public LectureServiceException(String message) {
        super(message);
    }
}
