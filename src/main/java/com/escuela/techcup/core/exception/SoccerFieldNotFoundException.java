package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class SoccerFieldNotFoundException extends TechcupException{

    public SoccerFieldNotFoundException(String soccerFieldId) {
        super("Soccer field not found with id: " + soccerFieldId, HttpStatus.NOT_FOUND);
    }

}
