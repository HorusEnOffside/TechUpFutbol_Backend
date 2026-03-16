package com.escuela.techcup.core.validator;

import com.escuela.techcup.core.util.ValidationUtil;

public class EmailValidator {
    
    public static boolean isValid(String email) {
 
        ValidationUtil.requireNotBlank(email, "email");
 
        return email.contains("@") && email.contains(".");
    }

}
