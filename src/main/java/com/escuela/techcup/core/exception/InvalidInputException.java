package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends TechcupException {
    public InvalidInputException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
