package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;


public class InvalidFileException extends TechcupException {

    public InvalidFileException(String message) {super(message, HttpStatus.BAD_REQUEST);}

}