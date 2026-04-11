package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.NormalPayment;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.model.enums.PaymentStatus;
import com.escuela.techcup.persistence.entity.payment.NormalPaymentEntity;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
import com.escuela.techcup.persistence.mapper.payment.PaymentMapper;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMapperTest {

    // ── toEntity(NormalPayment) ──────────────────────────────────────────

    @Test
    void toEntity_fromNormalPayment_returnsNullWhenNull() {
        assertNull(PaymentMapper.toEntity((NormalPayment) null));
    }

    @Test
    void toEntity_fromNormalPayment_mapsFieldsCorrectly() {
        NormalPayment model = new NormalPayment();
        model.setId("pay-1");
        model.setStatus(PaymentStatus.PENDING);
        model.setDescription("Pago inscripción");
        model.setPaymentDate(LocalDate.of(2025, 1, 15));
        model.setVoucher(null);

        NormalPaymentEntity entity = PaymentMapper.toEntity(model);

        assertNotNull(entity);
        assertEquals("pay-1", entity.getId());
        assertEquals(PaymentStatus.PENDING, entity.getStatus());
        assertEquals("Pago inscripción", entity.getDescription());
        assertEquals(LocalDate.of(2025, 1, 15), entity.getPaymentDate());
        assertNotNull(entity.getPaymentProof());
    }

    @Test
    void toEntity_fromNormalPayment_withVoucher_convertsToBytes() {
        NormalPayment model = new NormalPayment();
        model.setId("pay-2");
        model.setVoucher(new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));

        NormalPaymentEntity entity = PaymentMapper.toEntity(model);

        assertNotNull(entity);
        assertTrue(entity.getPaymentProof().length > 0);
    }

    // ── toEntity(Payment) ────────────────────────────────────────────────

    @Test
    void toEntity_fromPayment_returnsNullWhenNull() {
        assertNull(PaymentMapper.toEntity((Payment) null));
    }

    @Test
    void toEntity_fromPayment_mapsFieldsCorrectly() {
        NormalPayment model = new NormalPayment();
        model.setId("pay-3");
        model.setStatus(PaymentStatus.APPROVED);
        model.setDescription("desc");
        model.setPaymentDate(LocalDate.of(2025, 2, 1));

        NormalPaymentEntity entity = PaymentMapper.toEntity((Payment) model);

        assertNotNull(entity);
        assertEquals("pay-3", entity.getId());
        assertEquals(PaymentStatus.APPROVED, entity.getStatus());
    }

    // ── toModel(NormalPaymentEntity) ────────────────────────────────────

    @Test
    void toModel_fromNormalPaymentEntity_returnsNullWhenNull() {
        assertNull(PaymentMapper.toModel((NormalPaymentEntity) null));
    }

    @Test
    void toModel_fromNormalPaymentEntity_mapsFieldsCorrectly() {
        NormalPaymentEntity entity = new NormalPaymentEntity();
        entity.setId("pay-4");
        entity.setStatus(PaymentStatus.PENDING);
        entity.setDescription("desc");
        entity.setPaymentDate(LocalDate.of(2025, 3, 1));
        entity.setPaymentProof(new byte[0]);

        NormalPayment model = PaymentMapper.toModel(entity);

        assertNotNull(model);
        assertEquals("pay-4", model.getId());
        assertEquals(PaymentStatus.PENDING, model.getStatus());
        assertEquals("desc", model.getDescription());
        assertNull(model.getVoucher());
    }

    @Test
    void toModel_fromNormalPaymentEntity_withValidImageBytes_convertsToBufferedImage() {
        NormalPaymentEntity entity = new NormalPaymentEntity();
        entity.setId("pay-5");
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        NormalPayment temp = new NormalPayment();
        temp.setVoucher(img);
        NormalPaymentEntity withImage = PaymentMapper.toEntity(temp);
        entity.setPaymentProof(withImage.getPaymentProof());

        NormalPayment model = PaymentMapper.toModel(entity);

        assertNotNull(model.getVoucher());
    }

    // ── toModel(PaymentEntity) ───────────────────────────────────────────

    @Test
    void toModel_fromPaymentEntity_returnsNullWhenNull() {
        assertNull(PaymentMapper.toModel((PaymentEntity) null));
    }

    @Test
    void toModel_fromPaymentEntity_mapsFieldsCorrectly() {
        NormalPaymentEntity entity = new NormalPaymentEntity();
        entity.setId("pay-6");
        entity.setStatus(PaymentStatus.APPROVED);
        entity.setDescription("desc2");
        entity.setPaymentDate(LocalDate.of(2025, 4, 1));
        entity.setPaymentProof(new byte[0]);

        NormalPayment model = PaymentMapper.toModel((PaymentEntity) entity);

        assertNotNull(model);
        assertEquals("pay-6", model.getId());
        assertEquals(PaymentStatus.APPROVED, model.getStatus());
    }
}