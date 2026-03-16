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
        assertNotNull(PlayerStatus.valueOf("DISPONIBLE"));
        assertNotNull(PlayerStatus.valueOf("EN_EQUIPO"));
    }

    @Test
    void testNombreDeValores() {
        assertEquals("DISPONIBLE", PlayerStatus.DISPONIBLE.name());
        assertEquals("EN_EQUIPO",  PlayerStatus.EN_EQUIPO.name());
    }

    @Test
    void testValueOfInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> PlayerStatus.valueOf("INVALIDO"));
    }
}
