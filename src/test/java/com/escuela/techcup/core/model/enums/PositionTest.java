package com.escuela.techcup.core.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void testCantidadDeValores() {
        assertEquals(4, Position.values().length);
    }

    @Test
    void testValoresExisten() {
        assertNotNull(Position.valueOf("PORTERO"));
        assertNotNull(Position.valueOf("DEFENSA"));
        assertNotNull(Position.valueOf("VOLANTE"));
        assertNotNull(Position.valueOf("DELANTERO"));
    }

    @Test
    void testNombreDeValores() {
        assertEquals("PORTERO",   Position.PORTERO.name());
        assertEquals("DEFENSA",   Position.DEFENSA.name());
        assertEquals("VOLANTE",   Position.VOLANTE.name());
        assertEquals("DELANTERO", Position.DELANTERO.name());
    }

    @Test
    void testValueOfInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> Position.valueOf("INVALIDO"));
    }
}
