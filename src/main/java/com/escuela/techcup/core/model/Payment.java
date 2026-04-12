package com.escuela.techcup.core.model;

import java.time.LocalDateTime;
import com.escuela.techcup.core.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    private String id;
    private PaymentStatus status;
    private String description;
    private LocalDateTime paymentDate;
    private String urlComprobante;
}
