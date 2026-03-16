package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class TechcupException extends RuntimeException {

    private final HttpStatus status;

    public TechcupException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }


}