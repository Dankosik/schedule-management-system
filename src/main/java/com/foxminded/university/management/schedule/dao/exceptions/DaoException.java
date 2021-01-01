package com.foxminded.university.management.schedule.dao.exceptions;

public class DaoException extends RuntimeException{
    public DaoException(Throwable cause) {
        super(cause);
    }

    public DaoException(String message) {
        super(message);
    }
}
