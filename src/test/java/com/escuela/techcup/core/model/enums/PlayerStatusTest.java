package com.escuela.techcup.core.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerStatusTest {

    @Test
    void tresEstadosDefinidos() {
        assertEquals(3, PlayerStatus.values().length);
    }

    @Test
    void losEstadosExisten() {
        assertNotNull(PlayerStatus.valueOf("AVAILABLE"));
        assertNotNull(PlayerStatus.valueOf("INJURED"));
        assertNotNull(PlayerStatus.valueOf("NOT_AVAILABLE"));
    }

    @Test
    void nombresDeEstados() {
        assertEquals("AVAILABLE",     PlayerStatus.AVAILABLE.name());
        assertEquals("INJURED",       PlayerStatus.INJURED.name());
        assertEquals("NOT_AVAILABLE", PlayerStatus.NOT_AVAILABLE.name());
    }

    @Test
    void estadoInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> PlayerStatus.valueOf("EN_EQUIPO"));
    }
}
