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
        assertNotNull(TournamentStatus.valueOf("DRAFT"));
        assertNotNull(TournamentStatus.valueOf("ACTIVE"));
        assertNotNull(TournamentStatus.valueOf("IN_PROGRESS"));
        assertNotNull(TournamentStatus.valueOf("COMPLETED"));
    }

    @Test
    void testNombreDeValores() {
        assertEquals("DRAFT",       TournamentStatus.DRAFT.name());
        assertEquals("ACTIVE",      TournamentStatus.ACTIVE.name());
        assertEquals("IN_PROGRESS", TournamentStatus.IN_PROGRESS.name());
        assertEquals("COMPLETED",   TournamentStatus.COMPLETED.name());
    }

    @Test
    void testValueOfInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> TournamentStatus.valueOf("INVALIDO"));
    }
}
