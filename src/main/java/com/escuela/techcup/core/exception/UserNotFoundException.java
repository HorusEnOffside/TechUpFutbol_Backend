package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends TechcupException {
    public UserNotFoundException(String id) {
        super("User not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}