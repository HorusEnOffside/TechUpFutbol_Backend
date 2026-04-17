package com.escuela.techcup.core.model.enums;

/**
 * Catálogo de canchas predefinidas.
 * Cada valor lleva el nombre para mostrar y la URL de la imagen
 * que ya reside en el servidor (carpeta /static/canchas/).
 */
public enum CanchaTipo {

    CANCHA_1("Cancha 1", "/canchas/cancha_1.jpg"),
    CANCHA_2("Cancha 2", "/canchas/cancha_2.jpg"),
    CANCHA_3("Cancha 3", "/canchas/cancha_3.jpg"),
    CANCHA_4("Cancha 4", "/canchas/cancha_4.jpg"),
    CANCHA_5("Cancha 5", "/canchas/cancha_5.jpg");

    private final String displayName;
    private final String fotoUrl;

    CanchaTipo(String displayName, String fotoUrl) {
        this.displayName = displayName;
        this.fotoUrl = fotoUrl;
    }

    public String getDisplayName() { return displayName; }
    public String getFotoUrl()     { return fotoUrl; }
}
