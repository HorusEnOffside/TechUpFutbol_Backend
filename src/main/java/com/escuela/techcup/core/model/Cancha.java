package com.escuela.techcup.core.model;

import lombok.Data;

@Data
public class Cancha {
    private String id;
    private String nombre;
    /** Foto almacenada como bytes; puede ser null si no se subió imagen. */
    private byte[] foto;
}
