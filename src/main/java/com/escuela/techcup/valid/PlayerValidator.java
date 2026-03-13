package com.escuela.techcup.valid;

public final class PlayerValidator {
    
    public static void validateInput(String id, String name, String email, int dorsalNumber) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id es obligatorio");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name es obligatorio");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email es obligatorio");
        }
        if (dorsalNumber <= 0) {
            throw new IllegalArgumentException("El dorsal debe ser mayor a 0");
        }
    }

}
