package com.escuela.techcup.core.model;

import com.escuela.techcup.core.model.enums.PaymentStatus;

public interface Payment {
    PaymentStatus getStatus();
}
