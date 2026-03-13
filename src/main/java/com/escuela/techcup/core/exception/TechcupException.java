package com.escuela.techcup.core.exception;

public class TechcupException extends RuntimeException {

    public TechcupException(String message) {
        super(message);
    }

    public TechcupException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class InvalidInputException extends TechcupException {
        public InvalidInputException(String message) {
            super(message);
        }
    }

    public static class InvalidImageException extends TechcupException {
        public InvalidImageException(String message) {
            super(message);
        }
    }


}