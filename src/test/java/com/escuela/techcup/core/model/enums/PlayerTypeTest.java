package com.escuela.techcup.core.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTypeTest {

    @Test
    void testCantidadDeValores() {
        assertEquals(4, PlayerType.values().length);
    }

    @Test
    void testValoresExisten() {
        assertNotNull(PlayerType.valueOf("ESTUDIANTE"));
        assertNotNull(PlayerType.valueOf("PROFESOR"));
        assertNotNull(PlayerType.valueOf("GRADUADO"));
        assertNotNull(PlayerType.valueOf("FAMILIAR"));
    }

    @Test
    void testNombreDeValores() {
        assertEquals("ESTUDIANTE", PlayerType.ESTUDIANTE.name());
        assertEquals("PROFESOR",   PlayerType.PROFESOR.name());
        assertEquals("GRADUADO",   PlayerType.GRADUADO.name());
        assertEquals("FAMILIAR",   PlayerType.FAMILIAR.name());
    }

    @Test
    void testValueOfInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> PlayerType.valueOf("INVALIDO"));
    }
}
