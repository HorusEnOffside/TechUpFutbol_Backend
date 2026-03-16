package com.escuela.techcup.core.validator;

import com.escuela.techcup.core.util.ValidationUtil;

public final class PlayerValidator {
    
    public static void validateInput(String id, String name, String email, int dorsalNumber) {
        ValidationUtil.requireNotBlank(id, "id");
        ValidationUtil.requireNotBlank(name, "name");
        ValidationUtil.requireCorrectEmail(email);
        ValidationUtil.requirePositive(dorsalNumber, "dorsalNumber");
    }

}
