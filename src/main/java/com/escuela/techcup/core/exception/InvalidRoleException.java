package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class InvalidRoleException extends TechcupException {
    public InvalidRoleException(String role) {
        super("Invalid role: " + role, HttpStatus.BAD_REQUEST);
    }
}