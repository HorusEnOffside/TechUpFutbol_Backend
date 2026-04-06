package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class TournamentNotFoundException extends TechcupException {
    public TournamentNotFoundException(String id) {
        super("Tournament not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}