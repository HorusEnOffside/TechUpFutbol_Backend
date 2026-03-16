package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends TechcupException {
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
