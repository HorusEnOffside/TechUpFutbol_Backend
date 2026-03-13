package com.escuela.techcup.core.model;

import com.escuela.techcup.core.model.enums.PaymentStatus;

public class NormalPayment implements Payment {
    private PaymentStatus status;
    
    public PaymentStatus getStatus() {
        return status;
    }
}
