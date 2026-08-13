package com.fezaschools.fezasmart.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private OffsetDateTime timestamp;
    private String path;
    private List<ValidationError> errors;

    public ErrorResponse(int status, String error, String message, OffsetDateTime timestamp, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
    }
}
