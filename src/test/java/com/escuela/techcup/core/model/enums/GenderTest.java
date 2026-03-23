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
        assertNotNull(Gender.valueOf("MALE"));
        assertNotNull(Gender.valueOf("FEMALE"));
    }

    @Test
    void testNombreDeValores() {
        assertEquals("MALE", Gender.MALE.name());
        assertEquals("FEMALE",  Gender.FEMALE.name());
    }

    @Test
    void testValueOfInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> Gender.valueOf("INVALIDO"));
    }
}
