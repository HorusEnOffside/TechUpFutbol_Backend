package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PaymentRespondDTO {
    private String id;
    private PaymentStatus status;
    private String description;
    private LocalDateTime paymentDate;
    private String urlComprobante;
}
