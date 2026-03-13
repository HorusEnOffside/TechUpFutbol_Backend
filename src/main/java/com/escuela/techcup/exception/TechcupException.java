package com.escuela.techcup.exception;

public class TechcupException extends Exception {

    public TechcupException(String message) {
        super(message);
    }

    public static class InvalidUserException extends TechcupException {
        public InvalidUserException() {
            super("Usuario invalido");
        }
    }

    public static class InvalidPasswordException extends TechcupException {
        public InvalidPasswordException() {
            super("Contraseña invalida");
        }
    }

}