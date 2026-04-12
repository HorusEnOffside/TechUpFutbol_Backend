package com.escuela.techcup.persistence.mapper.payment;

import com.escuela.techcup.controller.dto.PaymentDTO;
import com.escuela.techcup.controller.dto.PaymentRespondDTO;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.model.enums.PaymentStatus;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMapperTest {

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2025, 3, 1, 10, 0);

    // ── Helpers ──────────────────────────────────────────────────────────

    private PaymentEntity buildEntity(String id) {
        PaymentEntity entity = new PaymentEntity();
        entity.setId(id);
        entity.setStatus(PaymentStatus.PENDING);
        entity.setDescription("Pago inscripcion");
        entity.setPaymentDate(FIXED_DATE);
        entity.setVoucher(new byte[]{1, 2, 3});
        entity.setVoucherType("image/jpeg");
        entity.setVoucherName("comprobante.jpg");
        entity.setVoucherSize(1024L);
        return entity;
    }

    private Payment buildModel(String id) {
        Payment model = new Payment();
        model.setId(id);
        model.setStatus(PaymentStatus.PENDING);
        model.setDescription("Pago inscripcion");
        model.setPaymentDate(FIXED_DATE);
        return model;
    }

    private PaymentDTO buildDTO() {
        PaymentDTO dto = new PaymentDTO();
        dto.setDescription("Pago inscripcion");
        dto.setPaymentDate(FIXED_DATE);
        return dto;
    }

    // ── toModel(PaymentEntity, String voucherUrl) ────────────────────────

    @Nested
    class ToModelFromEntityWithUrl {

        @Test
        void whenValidEntity_thenMapsAllFields() {
            PaymentEntity entity = buildEntity("pay-1");

            Payment result = PaymentMapper.toModel(entity, "/api/payments/pay-1/voucher");

            assertEquals("pay-1", result.getId());
            assertEquals(PaymentStatus.PENDING, result.getStatus());
            assertEquals("Pago inscripcion", result.getDescription());
            assertEquals(FIXED_DATE, result.getPaymentDate());
            assertEquals("/api/payments/pay-1/voucher", result.getUrlComprobante());
        }

        @Test
        void whenEntityIsNull_thenReturnsNull() {
            assertNull(PaymentMapper.toModel((PaymentEntity) null, "/api/payments/pay-1/voucher"));
        }

        @Test
        void whenVoucherUrlIsNull_thenMapsWithNullUrl() {
            PaymentEntity entity = buildEntity("pay-1");

            Payment result = PaymentMapper.toModel(entity, null);

            assertNotNull(result);
            assertNull(result.getUrlComprobante());
        }
    }

    // ── toModel(PaymentEntity) ───────────────────────────────────────────

    @Nested
    class ToModelFromEntity {

        @Test
        void whenValidEntity_thenMapsAllFields() {
            PaymentEntity entity = buildEntity("pay-1");

            Payment result = PaymentMapper.toModel(entity);

            assertEquals("pay-1", result.getId());
            assertEquals(PaymentStatus.PENDING, result.getStatus());
            assertEquals("Pago inscripcion", result.getDescription());
            assertEquals(FIXED_DATE, result.getPaymentDate());
        }

        @Test
        void whenEntityIsNull_thenReturnsNull() {
            assertNull(PaymentMapper.toModel((PaymentEntity) null));
        }
    }

    // ── toModel(PaymentDTO) ──────────────────────────────────────────────

    @Nested
    class ToModelFromDTO {

        @Test
        void whenValidDTO_thenMapsDescriptionAndDate() {
            PaymentDTO dto = buildDTO();

            Payment result = PaymentMapper.toModel(dto);

            assertEquals("Pago inscripcion", result.getDescription());
            assertEquals(FIXED_DATE, result.getPaymentDate());
        }

        @Test
        void whenDTOIsNull_thenReturnsNull() {
            assertNull(PaymentMapper.toModel((PaymentDTO) null));
        }

        @Test
        void whenValidDTO_thenIdIsNull() {
            Payment result = PaymentMapper.toModel(buildDTO());

            assertNull(result.getId());
        }
    }

    // ── toModel(String id, PaymentDTO) ───────────────────────────────────

    @Nested
    class ToModelFromIdAndDTO {

        @Test
        void whenValidIdAndDTO_thenMapsAllFields() {
            PaymentDTO dto = buildDTO();

            Payment result = PaymentMapper.toModel("pay-1", dto);

            assertEquals("pay-1", result.getId());
            assertEquals("Pago inscripcion", result.getDescription());
            assertEquals(FIXED_DATE, result.getPaymentDate());
        }

        @Test
        void whenDTOIsNull_thenReturnsNull() {
            assertNull(PaymentMapper.toModel("pay-1", null));
        }
    }

    // ── toEntity(Payment, byte[], ...) ───────────────────────────────────

    @Nested
    class ToEntityWithVoucher {

        @Test
        void whenValidModel_thenMapsAllFields() {
            Payment model = buildModel("pay-1");
            byte[] bytes = new byte[]{1, 2, 3};

            PaymentEntity result = PaymentMapper.toEntity(model, bytes, "image/jpeg", "comprobante.jpg", 1024L);

            assertEquals("pay-1", result.getId());
            assertEquals(PaymentStatus.PENDING, result.getStatus());
            assertEquals("Pago inscripcion", result.getDescription());
            assertEquals(FIXED_DATE, result.getPaymentDate());
            assertArrayEquals(bytes, result.getVoucher());
            assertEquals("image/jpeg", result.getVoucherType());
            assertEquals("comprobante.jpg", result.getVoucherName());
            assertEquals(1024L, result.getVoucherSize());
        }

        @Test
        void whenModelIsNull_thenReturnsNull() {
            assertNull(PaymentMapper.toEntity(null, new byte[]{1}, "image/jpeg", "file.jpg", 1L));
        }
    }

    // ── toEntity(Payment) ────────────────────────────────────────────────

    @Nested
    class ToEntityWithoutVoucher {

        @Test
        void whenValidModel_thenMapsBaseFields() {
            Payment model = buildModel("pay-1");

            PaymentEntity result = PaymentMapper.toEntity(model);

            assertEquals("pay-1", result.getId());
            assertEquals(PaymentStatus.PENDING, result.getStatus());
            assertEquals("Pago inscripcion", result.getDescription());
            assertEquals(FIXED_DATE, result.getPaymentDate());
        }

        @Test
        void whenModelIsNull_thenReturnsNull() {
            assertNull(PaymentMapper.toEntity(null));
        }

        @Test
        void whenValidModel_thenVoucherFieldsAreNull() {
            PaymentEntity result = PaymentMapper.toEntity(buildModel("pay-1"));

            assertNull(result.getVoucher());
            assertNull(result.getVoucherType());
            assertNull(result.getVoucherName());
            assertNull(result.getVoucherSize());
        }
    }

    // ── toRespondDTO ─────────────────────────────────────────────────────

    @Nested
    class ToRespondDTO {

        @Test
        void whenValidModel_thenMapsAllFields() {
            Payment model = buildModel("pay-1");
            model.setUrlComprobante("/api/payments/pay-1/voucher");

            PaymentRespondDTO result = PaymentMapper.toRespondDTO(model);

            assertEquals("pay-1", result.getId());
            assertEquals(PaymentStatus.PENDING, result.getStatus());
            assertEquals("Pago inscripcion", result.getDescription());
            assertEquals(FIXED_DATE, result.getPaymentDate());
            assertEquals("/api/payments/pay-1/voucher", result.getUrlComprobante());
        }

        @Test
        void whenModelIsNull_thenReturnsNull() {
            assertNull(PaymentMapper.toRespondDTO(null));
        }
    }

    // ── updateEntity ─────────────────────────────────────────────────────

    @Nested
    class UpdateEntity {

        @Test
        void whenValidArgs_thenUpdatesFields() {
            PaymentEntity entity = buildEntity("pay-1");
            Payment model = buildModel("pay-1");
            model.setDescription("Nueva descripcion");
            model.setStatus(PaymentStatus.APPROVED);

            PaymentMapper.updateEntity(entity, model);

            assertEquals("Nueva descripcion", entity.getDescription());
            assertEquals(PaymentStatus.APPROVED, entity.getStatus());
        }

        @Test
        void whenEntityIsNull_thenDoesNothing() {
            assertDoesNotThrow(() -> PaymentMapper.updateEntity(null, buildModel("pay-1")));
        }

        @Test
        void whenModelIsNull_thenDoesNothing() {
            assertDoesNotThrow(() -> PaymentMapper.updateEntity(buildEntity("pay-1"), null));
        }
    }

    // ── updateEntityWithVoucher ──────────────────────────────────────────

    @Nested
    class UpdateEntityWithVoucher {

        @Test
        void whenVoucherBytesProvided_thenUpdatesVoucherFields() {
            PaymentEntity entity = buildEntity("pay-1");
            Payment model = buildModel("pay-1");
            byte[] newBytes = new byte[]{9, 8, 7};

            PaymentMapper.updateEntityWithVoucher(entity, model, newBytes, "application/pdf", "nuevo.pdf", 2048L);

            assertArrayEquals(newBytes, entity.getVoucher());
            assertEquals("application/pdf", entity.getVoucherType());
            assertEquals("nuevo.pdf", entity.getVoucherName());
            assertEquals(2048L, entity.getVoucherSize());
        }

        @Test
        void whenVoucherBytesAreNull_thenDoesNotUpdateVoucherFields() {
            PaymentEntity entity = buildEntity("pay-1");
            byte[] originalBytes = entity.getVoucher();
            Payment model = buildModel("pay-1");

            PaymentMapper.updateEntityWithVoucher(entity, model, null, null, null, null);

            assertArrayEquals(originalBytes, entity.getVoucher());
        }

        @Test
        void whenEntityIsNull_thenDoesNothing() {
            assertDoesNotThrow(() -> PaymentMapper.updateEntityWithVoucher(
                    null, buildModel("pay-1"), new byte[]{1}, "image/jpeg", "f.jpg", 1L));
        }

        @Test
        void whenModelIsNull_thenDoesNothing() {
            assertDoesNotThrow(() -> PaymentMapper.updateEntityWithVoucher(
                    buildEntity("pay-1"), null, new byte[]{1}, "image/jpeg", "f.jpg", 1L));
        }
    }

    // ── updateModel ──────────────────────────────────────────────────────

    @Nested
    class UpdateModel {

        @Test
        void whenValidArgs_thenUpdatesFields() {
            Payment model = buildModel("pay-1");
            PaymentDTO dto = new PaymentDTO();
            dto.setDescription("Descripcion actualizada");
            dto.setPaymentDate(LocalDateTime.of(2025, 6, 1, 0, 0));

            PaymentMapper.updateModel(model, dto);

            assertEquals("Descripcion actualizada", model.getDescription());
            assertEquals(LocalDateTime.of(2025, 6, 1, 0, 0), model.getPaymentDate());
        }

        @Test
        void whenModelIsNull_thenDoesNothing() {
            assertDoesNotThrow(() -> PaymentMapper.updateModel(null, buildDTO()));
        }

        @Test
        void whenDTOIsNull_thenDoesNothing() {
            assertDoesNotThrow(() -> PaymentMapper.updateModel(buildModel("pay-1"), null));
        }
    }

    // ── getVoucherMetadata ───────────────────────────────────────────────

    @Nested
    class GetVoucherMetadata {

        @Test
        void whenValidEntity_thenReturnsMetadata() {
            PaymentEntity entity = buildEntity("pay-1");

            PaymentMapper.VoucherMetadata result = PaymentMapper.getVoucherMetadata(entity);

            assertNotNull(result);
            assertArrayEquals(new byte[]{1, 2, 3}, result.getBytes());
            assertEquals("image/jpeg", result.getType());
            assertEquals("comprobante.jpg", result.getName());
            assertEquals(1024L, result.getSize());
        }

        @Test
        void whenEntityIsNull_thenReturnsNull() {
            assertNull(PaymentMapper.getVoucherMetadata(null));
        }
    }
}
