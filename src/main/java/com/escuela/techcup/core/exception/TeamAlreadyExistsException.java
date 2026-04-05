package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class TeamAlreadyExistsException extends TechcupException {
    public TeamAlreadyExistsException(String name) {
        super("Team already exists with name: " + name, HttpStatus.CONFLICT);
    }
}