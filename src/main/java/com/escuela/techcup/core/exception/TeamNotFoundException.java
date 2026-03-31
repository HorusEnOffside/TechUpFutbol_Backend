package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class TeamNotFoundException extends TechcupException{

    public TeamNotFoundException(String teamId) {
        super("Team with ID " + teamId + " not found.", HttpStatus.NOT_FOUND);
    }

}
