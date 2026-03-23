package com.escuela.techcup.core.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerStatusTest {

    @Test
    void testCantidadDeValores() {
        assertEquals(2, PlayerStatus.values().length);
    }

    @Test
    void testValoresExisten() {
        assertNotNull(PlayerStatus.valueOf("AVAILABLE"));
        assertNotNull(PlayerStatus.valueOf("IN_TEAM"));
    }

    @Test
    void testNombreDeValores() {
        assertEquals("AVAILABLE", PlayerStatus.AVAILABLE.name());
        assertEquals("IN_TEAM",   PlayerStatus.IN_TEAM.name());
    }

    @Test
    void testValueOfInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> PlayerStatus.valueOf("INVALIDO"));
    }
}
