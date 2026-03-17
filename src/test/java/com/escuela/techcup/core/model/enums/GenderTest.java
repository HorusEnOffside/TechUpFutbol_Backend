package com.escuela.techcup.core.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenderTest {

    @Test
    void testCantidadDeValores() {
        assertEquals(2, Gender.values().length);
    }

    @Test
    void testValoresExisten() {
        assertNotNull(Gender.valueOf("HOMBRE"));
        assertNotNull(Gender.valueOf("MUJER"));
    }

    @Test
    void testNombreDeValores() {
        assertEquals("HOMBRE", Gender.HOMBRE.name());
        assertEquals("MUJER",  Gender.MUJER.name());
    }

    @Test
    void testValueOfInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> Gender.valueOf("INVALIDO"));
    }
}
