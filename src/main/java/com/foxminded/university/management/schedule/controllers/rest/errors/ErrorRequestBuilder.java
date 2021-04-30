package com.foxminded.university.management.schedule.controllers.rest.errors;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Objects;

public class ErrorRequestBuilder {
    @Schema(description = "Error",
            example = "CONFLICT", required = true)
    private final HttpStatus error;
    @Schema(description = "Message",
            example = "Audience with number 2 is already exist", required = true)
    private final String message;
    @Schema(description = "Timestamp",
            example = "2021-04-30T05:22:10.479+00:00", required = true)
    private Date timestamp;
    @Schema(description = "Status",
            example = "409", required = true)
    private int status;

    public ErrorRequestBuilder(HttpStatus status, String message) {
        super();
        this.error = status;
        this.message = message;
    }

    public ErrorRequestBuilder(HttpStatus error, String message, int status) {
        super();
        this.error = error;
        this.message = message;
        this.status = status;
    }

    public ErrorRequestBuilder(Date timestamp, HttpStatus error, String message, int status) {
        super();
        this.timestamp = timestamp;
        this.error = error;
        this.message = message;
        this.status = status;
    }

    public HttpStatus getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorRequestBuilder that = (ErrorRequestBuilder) o;
        return status == that.status && error == that.error && Objects.equals(message, that.message) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, message, timestamp, status);
    }

    @Override
    public String toString() {
        return "ErrorRequestBuilder{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", status=" + status +
                '}';
    }
}
