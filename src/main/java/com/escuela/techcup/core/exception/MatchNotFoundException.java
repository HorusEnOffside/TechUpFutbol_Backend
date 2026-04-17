package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class MatchNotFoundException extends TechcupException{

    public MatchNotFoundException(String matchId) {
        super("Match not found with id: " + matchId, HttpStatus.NOT_FOUND);
    }

}
