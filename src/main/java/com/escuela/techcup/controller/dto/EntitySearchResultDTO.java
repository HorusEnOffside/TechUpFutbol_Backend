package com.escuela.techcup.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/** Respuesta minima { id, name } para busqueda de equipo o jugador en el flujo de sanciones. */
@Data
@AllArgsConstructor
public class EntitySearchResultDTO {
    private String id;
    private String name;
}
