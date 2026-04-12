package com.escuela.techcup.core.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class PaymentDateException extends TechcupException {
    public PaymentDateException(LocalDateTime paymentDate, LocalDateTime deadline) {
        super(
                "La fecha del pago [" + paymentDate + "] supera la fecha límite del torneo [" + deadline + "]",
                HttpStatus.BAD_REQUEST
        );
    }
}