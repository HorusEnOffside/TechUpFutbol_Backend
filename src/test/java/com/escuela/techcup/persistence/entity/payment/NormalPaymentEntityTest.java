package com.escuela.techcup.persistence.entity.payment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NormalPaymentEntityTest {
    @Test
    void canSetAndGetFields() {
        NormalPaymentEntity entity = new NormalPaymentEntity();
        entity.setId("np1");
        assertEquals("np1", entity.getId());
    }
}
