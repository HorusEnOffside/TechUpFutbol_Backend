package com.escuela.techcup.core.validator;


import com.escuela.techcup.core.util.ValidationUtil;
import com.escuela.techcup.core.model.enums.Formation;
import java.awt.Color;
import java.util.List;

public final class TeamValidator {

    private TeamValidator() {
    }

    public static void validateInput(String name, List<Color> uniformColors, Formation formation) {
        ValidationUtil.requireNotBlank(name, "name");
        ValidationUtil.requireNotNull(uniformColors, "uniformColors");
        ValidationUtil.requireNotNull(formation, "formation");
    }
}