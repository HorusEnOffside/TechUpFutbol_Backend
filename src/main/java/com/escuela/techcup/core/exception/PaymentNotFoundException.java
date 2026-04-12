package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

public class PaymentNotFoundException extends TechcupException {

    public PaymentNotFoundException(String message) {super(message, HttpStatus.NOT_FOUND);}

}
