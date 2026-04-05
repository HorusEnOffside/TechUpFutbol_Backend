package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class InvitationNotFoundException extends TechcupException {
    public InvitationNotFoundException(String id) {
        super("Invitation not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}