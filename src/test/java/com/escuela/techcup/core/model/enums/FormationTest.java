package com.escuela.techcup.core.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FormationTest {

    @Test
    void testCantidadDeValores() {
        assertEquals(2, Formation.values().length);
    }
}
