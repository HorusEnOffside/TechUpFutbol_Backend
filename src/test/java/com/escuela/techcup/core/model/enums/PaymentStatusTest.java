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
        assertNotNull(PaymentStatus.valueOf("PENDING"));
        assertNotNull(PaymentStatus.valueOf("IN_REVIEW"));
        assertNotNull(PaymentStatus.valueOf("APPROVED"));
        assertNotNull(PaymentStatus.valueOf("REJECTED"));
    }

    @Test
    void testNombreDeValores() {
        assertEquals("PENDING",   PaymentStatus.PENDING.name());
        assertEquals("IN_REVIEW", PaymentStatus.IN_REVIEW.name());
        assertEquals("APPROVED",  PaymentStatus.APPROVED.name());
        assertEquals("REJECTED",  PaymentStatus.REJECTED.name());
    }

    @Test
    void testValueOfInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> PaymentStatus.valueOf("INVALIDO"));
    }
}
