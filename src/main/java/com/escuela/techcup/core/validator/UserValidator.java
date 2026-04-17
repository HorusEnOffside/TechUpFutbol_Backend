package com.escuela.techcup.core.validator;

import java.time.LocalDate;

import com.escuela.techcup.core.util.DateUtil;
import com.escuela.techcup.core.util.ValidationUtil;

public final class UserValidator {

    private UserValidator() {
    }

    public static void validateInput(String name, String mail, String password, LocalDate dateOfBirth) {
        ValidationUtil.requireNotBlank(name, "name");
        ValidationUtil.requireCorrectMail(mail);
        DateUtil.isPast(dateOfBirth);
        ValidationUtil.passwordRules(password);
    }

}
