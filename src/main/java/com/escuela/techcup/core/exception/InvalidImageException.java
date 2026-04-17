package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class InvalidImageException extends TechcupException {

    public InvalidImageException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }


}
