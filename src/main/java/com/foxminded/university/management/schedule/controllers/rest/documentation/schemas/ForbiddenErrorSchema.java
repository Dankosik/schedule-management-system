package com.foxminded.university.management.schedule.controllers.rest.documentation.schemas;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.util.Date;

public class ForbiddenErrorSchema {
    @Schema(description = "Error",
            example = "Forbidden", required = true)
    private HttpStatus error;
    @Schema(description = "Error message",
            example = "Forbidden", required = true)
    private String message;
    @Schema(description = "Timestamp",
            example = "2021-04-30T05:22:10.479+00:00", required = true)
    private Date timestamp;
    @Schema(description = "Status",
            example = "403", required = true)
    private int status;
    @Schema(description = "Path",
            example = "/api/v1/teachers/1", required = true)
    private String path;

    public HttpStatus getError() {
        return error;
    }

    public void setError(HttpStatus error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
