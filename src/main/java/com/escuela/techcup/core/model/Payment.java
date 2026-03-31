package com.escuela.techcup.core.model;

import java.awt.image.BufferedImage;
import java.time.LocalDate;


import com.escuela.techcup.core.model.enums.PaymentStatus;

public interface Payment {
    String getId();
    PaymentStatus getStatus();
    String getDescription();
    LocalDate getPaymentDate();
    BufferedImage getVoucher();
}
