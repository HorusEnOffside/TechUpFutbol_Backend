package com.escuela.techcup.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class PaymentDTO {

    private String description;

    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDateTime paymentDate;

}
