package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class FileStorageException extends TechcupException {
    public FileStorageException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}