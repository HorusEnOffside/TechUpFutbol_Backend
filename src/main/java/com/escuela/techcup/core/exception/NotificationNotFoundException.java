package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends TechcupException {

    public NotificationNotFoundException(String id) {
        super("Notification not found with ID " + id, HttpStatus.NOT_FOUND);
    }
}
