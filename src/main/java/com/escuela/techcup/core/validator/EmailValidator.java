package com.escuela.techcup.core.validator;

public class EmailValidator {
    
    public static boolean isValid(String email) {
 
        if (email == null || email.isBlank()) {
            return false;
        }
 
        return email.contains("@") && email.contains(".");
    }

}
