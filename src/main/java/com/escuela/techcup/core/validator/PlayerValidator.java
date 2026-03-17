package com.escuela.techcup.core.validator;

import com.escuela.techcup.core.util.ValidationUtil;

public final class PlayerValidator {

    private PlayerValidator() {
    }
    
    public static void validateInput(int dorsalNumber) {
        ValidationUtil.requirePositive(dorsalNumber, "dorsalNumber");
    }

}
