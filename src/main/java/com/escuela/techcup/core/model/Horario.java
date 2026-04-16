package com.escuela.techcup.core.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Horario {
    private String id;
    private LocalDate fecha;
    private String descripcion;
}
