package com.escuela.techcup.core.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TournamentStatusTest {

    @Test
    void testCantidadDeValores() {
        assertEquals(4, TournamentStatus.values().length);
    }

    @Test
    void testValoresExisten() {
        assertNotNull(TournamentStatus.valueOf("BORRADOR"));
        assertNotNull(TournamentStatus.valueOf("ACTIVO"));
        assertNotNull(TournamentStatus.valueOf("EN_PROCESO"));
        assertNotNull(TournamentStatus.valueOf("FINALIZADO"));
    }

    @Test
    void testNombreDeValores() {
        assertEquals("BORRADOR",   TournamentStatus.BORRADOR.name());
        assertEquals("ACTIVO",     TournamentStatus.ACTIVO.name());
        assertEquals("EN_PROCESO", TournamentStatus.EN_PROCESO.name());
        assertEquals("FINALIZADO", TournamentStatus.FINALIZADO.name());
    }

    @Test
    void testValueOfInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> TournamentStatus.valueOf("INVALIDO"));
    }
}
