package com.escuela.techcup.core.util;

import com.escuela.techcup.core.exception.ValidationException;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static void requireNotNull(Object value, String fieldName) {
		if (value == null) {
            throw new ValidationException(fieldName + " cannot be null");
		}
	}

    public static void requireNotBlank(String value, String fieldName) {
		requireNotNull(value, fieldName);
		if (value.isBlank()) {
            throw new ValidationException(fieldName + " cannot be blank");
		}
	}

    public static void requireCorrectMail(String mail) {
        requireNotBlank(mail, "mail");
        if (!mail.contains("@") || !mail.contains(".")) {
            throw new ValidationException("mail is invalid");
        }
    }

    public static void requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException(fieldName + " must be greater than 0");
        }
    }

    public static void passwordRules(String password) { // password logic
        requireNotBlank(password, "password");
        if (password.length() < 8) {
            throw new ValidationException("password must have at least 8 characters");
        }
        if (!containsUppercase(password)) {
            throw new ValidationException("password must contain at least one uppercase letter");
        }
    }

    private static boolean containsUppercase(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (Character.isUpperCase(value.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static void semesterRules(int semester) {
        requirePositive(semester, "semester");
        if (semester > 10) {
            throw new ValidationException("semester must be less than or equal to 10");
        }
    }
}