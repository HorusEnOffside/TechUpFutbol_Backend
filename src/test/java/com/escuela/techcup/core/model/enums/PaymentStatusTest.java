package com.escuela.techcup.core.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentStatusTest {

    @Test
    void testCantidadDeValores() {
        assertEquals(4, PaymentStatus.values().length);
    }

    @Test
    void testValoresExisten() {
        assertNotNull(PaymentStatus.valueOf("PENDIENTE"));
        assertNotNull(PaymentStatus.valueOf("EN_REVISION"));
        assertNotNull(PaymentStatus.valueOf("APROBADO"));
        assertNotNull(PaymentStatus.valueOf("RECHAZADO"));
    }

    @Test
    void testNombreDeValores() {
        assertEquals("PENDIENTE",   PaymentStatus.PENDIENTE.name());
        assertEquals("EN_REVISION", PaymentStatus.EN_REVISION.name());
        assertEquals("APROBADO",    PaymentStatus.APROBADO.name());
        assertEquals("RECHAZADO",   PaymentStatus.RECHAZADO.name());
    }

    @Test
    void testValueOfInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> PaymentStatus.valueOf("INVALIDO"));
    }
}
