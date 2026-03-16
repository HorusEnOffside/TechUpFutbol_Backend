package com.escuela.techcup.core.model;

import com.escuela.techcup.core.model.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NormalPaymentTest {

    @Test
    void testGetStatusPorDefectoEsNulo() {
        NormalPayment payment = new NormalPayment();
        assertNull(payment.getStatus());
    }

    @Test
    void testNormalPaymentNoEsNulo() {
        NormalPayment payment = new NormalPayment();
        assertNotNull(payment);
    }

    @Test
    void testImplementaPayment() {
        NormalPayment payment = new NormalPayment();
        assertInstanceOf(Payment.class, payment);
    }

    @Test
    void testGetStatusRetornaTipoPaymentStatus() {
        NormalPayment payment = new NormalPayment();
        // sin setters, el status es null — verificamos que no lanza excepción
        assertDoesNotThrow(payment::getStatus);
    }
}
