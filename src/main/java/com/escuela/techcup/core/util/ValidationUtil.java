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

    public static void requireCorrectMail(String mail) {
        requireNotBlank(mail, "mail");
        if (!mail.contains("@") || !mail.contains(".")) {
            throw new ValidationException("mail es invalido");
        }
    }

    public static void requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException(fieldName + " debe ser mayor a 0");
        }
    }

    public static void passwordRules(String password) { //logica password
        requireNotBlank(password, "password");
        if (password.length() < 8) {
            throw new ValidationException("password debe tener al menos 8 caracteres");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new ValidationException("password debe contener al menos una letra mayúscula");
        }
    }

    public static void semesterRules(int semester) {
        requirePositive(semester, "semester");
        if (semester > 10) {
            throw new ValidationException("semester debe ser menor o igual a 10");
        }
    }
}