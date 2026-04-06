package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class TournamentNotActiveException extends TechcupException {
    public TournamentNotActiveException() {
        super("No active tournament found", HttpStatus.BAD_REQUEST);
    }
}