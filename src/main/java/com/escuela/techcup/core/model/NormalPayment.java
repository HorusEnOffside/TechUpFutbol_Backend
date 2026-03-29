package com.escuela.techcup.core.model;

import java.time.LocalDate;
import java.awt.image.BufferedImage;

import com.escuela.techcup.core.model.enums.PaymentStatus;

import lombok.Data;

@Data
public class NormalPayment implements Payment {
    private PaymentStatus status;
    private BufferedImage voucher;
    private String description;
    private LocalDate paymentDate;

    
}
