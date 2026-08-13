package com.fezaschools.fezasmart.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ValidationException extends RuntimeException {

    private final List<ErrorResponse.ValidationError> errors;

    public ValidationException(String message, List<ErrorResponse.ValidationError> errors) {
        super(message);
        this.errors = errors;
    }

    public List<ErrorResponse.ValidationError> getErrors() {
        return errors;
    }
}
