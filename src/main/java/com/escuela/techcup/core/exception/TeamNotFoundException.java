package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class TeamNotFoundException extends TechcupException {
    public TeamNotFoundException(String id) {
        super("Team not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}