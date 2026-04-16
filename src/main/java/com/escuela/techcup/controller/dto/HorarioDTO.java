package com.escuela.techcup.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HorarioDTO {

    @NotNull(message = "fecha del horario es requerida")
    private LocalDate fecha;

    @NotBlank(message = "descripcion del horario es requerida")
    private String descripcion;
}
