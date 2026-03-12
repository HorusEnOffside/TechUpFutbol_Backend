package com.escuela.techcup.model;

import com.escuela.techcup.model.enums.PaymentStatus;

public interface Payment {
    PaymentStatus getStatus();
}
