package com.escuela.techcup.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class PaymentDTO {

    @NotNull(message = "El comprobante es obligatorio")
    private MultipartFile comprobante;

    private String description;

    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDateTime paymentDate;

}
