package com.escuela.techcup.core.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvitationStatusTest {

    @Test
    void testCantidadDeValores() {
        assertEquals(2, InvitationStatus.values().length);
    }

    @Test
    void testValoresExisten() {
        assertNotNull(InvitationStatus.valueOf("ACCEPTED"));
        assertNotNull(InvitationStatus.valueOf("REJECTED"));
    }

    @Test
    void testNombreDeValores() {
        assertEquals("ACCEPTED", InvitationStatus.ACCEPTED.name());
        assertEquals("REJECTED", InvitationStatus.REJECTED.name());
    }

    @Test
    void testValueOfInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> InvitationStatus.valueOf("INVALIDO"));
    }
}
