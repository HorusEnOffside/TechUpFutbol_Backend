package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends TechcupException {

    public NotificationNotFoundException(Long userId) {
        super("No notifications found for user with ID " + userId, HttpStatus.NOT_FOUND);
    }
}
