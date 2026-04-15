package com.escuela.techcup.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateSancionDTO {

    /** "equipo" o "jugador" */
    @NotBlank
    @Pattern(regexp = "equipo|jugador", message = "tipo debe ser 'equipo' o 'jugador'")
    private String tipo;

    @NotBlank
    private String entidadId;

    @NotBlank
    private String entidadNombre;

    @NotBlank
    private String motivo;

    @NotNull
    private LocalDate fecha;
}
