package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class PlayerAlreadyInvitedException extends TechcupException {
    public PlayerAlreadyInvitedException(String playerId) {
        super("Player already has an invitation from this team: " + playerId, HttpStatus.CONFLICT);
    }
}