package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class TournamentFinalizedException extends TechcupException {
    public TournamentFinalizedException(String id) {
        super("Tournament is already finalized: " + id, HttpStatus.CONFLICT);
    }
}