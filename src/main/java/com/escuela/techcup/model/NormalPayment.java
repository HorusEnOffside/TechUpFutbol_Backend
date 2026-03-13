package com.escuela.techcup.model;

import com.escuela.techcup.model.enums.PaymentStatus;

public class NormalPayment implements Payment {
    private PaymentStatus status;
    
    public PaymentStatus getStatus() {
        return status;
    }
}
