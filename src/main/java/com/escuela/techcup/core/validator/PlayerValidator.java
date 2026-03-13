package com.escuela.techcup.core.validator;

import com.escuela.techcup.core.exception.TechcupException;

public final class PlayerValidator {
    
    public static void validateInput(String id, String name, String email, int dorsalNumber) {
        if (id == null || id.isBlank()) {
            throw new TechcupException.InvalidInputException("id es obligatorio");
        }
        if (name == null || name.isBlank()) {
            throw new TechcupException.InvalidInputException("name es obligatorio");
        }
        if (email == null || email.isBlank() || !EmailValidator.isValid(email)) {
            throw new TechcupException.InvalidInputException("email es obligatorio");
        }
        if (dorsalNumber <= 0) {
            throw new TechcupException.InvalidInputException("El dorsal debe ser mayor a 0");
        }
    }

}
