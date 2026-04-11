package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class TournamentOverlapException extends TechcupException {
    public TournamentOverlapException() {
        super("A tournament already exists with overlapping dates", HttpStatus.CONFLICT);
    }
}