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
        assertNotNull(InvitationStatus.valueOf("ACEPTAR"));
        assertNotNull(InvitationStatus.valueOf("RECHAZAR"));
    }

    @Test
    void testNombreDeValores() {
        assertEquals("ACEPTAR",  InvitationStatus.ACEPTAR.name());
        assertEquals("RECHAZAR", InvitationStatus.RECHAZAR.name());
    }

    @Test
    void testValueOfInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> InvitationStatus.valueOf("INVALIDO"));
    }
}
