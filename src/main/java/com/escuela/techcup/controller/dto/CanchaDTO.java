package com.escuela.techcup.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CanchaDTO {

    @NotBlank(message = "nombre de cancha es requerido")
    private String nombre;

    /**
     * Foto de la cancha codificada en Base64 (opcional).
     * El backend decodifica y almacena como bytes.
     */
    private String foto;
}
