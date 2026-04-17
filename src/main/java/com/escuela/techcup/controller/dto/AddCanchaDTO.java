package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.CanchaTipo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCanchaDTO {

    @NotNull(message = "tipo de cancha es requerido")
    private CanchaTipo tipo;

    /** Opcional: si no se envía se usa el nombre del catálogo (ej. "Cancha 1"). */
    private String nombre;
}
