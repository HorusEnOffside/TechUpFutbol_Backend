package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class ScheduleConflictException extends TechcupException {
    public ScheduleConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}