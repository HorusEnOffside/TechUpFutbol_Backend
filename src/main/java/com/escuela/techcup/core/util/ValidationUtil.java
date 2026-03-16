package com.escuela.techcup.core.util;

import com.escuela.techcup.core.exception.ValidationException;

public class ValidationUtil {

    public static void requireNotNull(Object value, String fieldName) {
		if (value == null) {
			throw new ValidationException(fieldName + " no puede ser nulo");
		}
	}

    public static void requireNotBlank(String value, String fieldName) {
		requireNotNull(value, fieldName);
		if (value.isBlank()) {
			throw new ValidationException(fieldName + " no puede estar vacio");
		}
	}

    public static void requireCorrectEmail(String email) {
        requireNotBlank(email, "email");
        if (!email.contains("@") || !email.contains(".")) {
            throw new ValidationException("email es invalido");
        }
    }

    public static void requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException(fieldName + " debe ser mayor a 0");
        }
    }
}