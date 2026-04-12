package com.escuela.techcup.persistence.mapper.payment;

import com.escuela.techcup.controller.dto.PaymentDTO;
import com.escuela.techcup.controller.dto.PaymentRespondDTO;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;

public class PaymentMapper {

    private PaymentMapper() {
    }

    public static Payment toModel(PaymentEntity entity, String voucherUrl) {
        if (entity == null) {
            return null;
        }

        return new Payment(
                entity.getId(),
                entity.getStatus(),
                entity.getDescription(),
                entity.getPaymentDate(),
                voucherUrl
        );
    }

    public static PaymentEntity toEntity(
            Payment model,
            byte[] voucherBytes,
            String voucherType,
            String voucherName,
            Long voucherSize) {

        if (model == null) {
            return null;
        }

        PaymentEntity entity = new PaymentEntity();
        entity.setId(model.getId());
        entity.setStatus(model.getStatus());
        entity.setDescription(model.getDescription());
        entity.setPaymentDate(model.getPaymentDate());
        entity.setVoucher(voucherBytes);
        entity.setVoucherType(voucherType);
        entity.setVoucherName(voucherName);
        entity.setVoucherSize(voucherSize);

        return entity;
    }

    public static PaymentEntity toEntity(Payment model) {
        if (model == null) {
            return null;
        }

        PaymentEntity entity = new PaymentEntity();
        entity.setId(model.getId());
        entity.setStatus(model.getStatus());
        entity.setDescription(model.getDescription());
        entity.setPaymentDate(model.getPaymentDate());

        return entity;
    }

    public static void updateEntity(PaymentEntity entity, Payment model) {
        if (entity == null || model == null) {
            return;
        }
        entity.setStatus(model.getStatus());
        entity.setDescription(model.getDescription());
        entity.setPaymentDate(model.getPaymentDate());
    }

    public static void updateEntityWithVoucher(
            PaymentEntity entity,
            Payment model,
            byte[] voucherBytes,
            String voucherType,
            String voucherName,
            Long voucherSize) {

        if (entity == null || model == null) {
            return;
        }

        entity.setStatus(model.getStatus());
        entity.setDescription(model.getDescription());
        entity.setPaymentDate(model.getPaymentDate());

        if (voucherBytes != null) {
            entity.setVoucher(voucherBytes);
            entity.setVoucherType(voucherType);
            entity.setVoucherName(voucherName);
            entity.setVoucherSize(voucherSize);
        }
    }

    public static Payment toModel(PaymentDTO dto) {
        if (dto == null) {
            return null;
        }

        Payment model = new Payment();
        model.setDescription(dto.getDescription());
        model.setPaymentDate(dto.getPaymentDate());

        return model;
    }

    public static Payment toModel(String id, PaymentDTO dto) {
        if (dto == null) {
            return null;
        }

        Payment model = new Payment();
        model.setId(id);
        model.setDescription(dto.getDescription());
        model.setPaymentDate(dto.getPaymentDate());

        return model;
    }

    public static PaymentRespondDTO toRespondDTO(Payment model) {
        if (model == null) {
            return null;
        }

        PaymentRespondDTO dto = new PaymentRespondDTO();
        dto.setId(model.getId());
        dto.setStatus(model.getStatus());
        dto.setDescription(model.getDescription());
        dto.setPaymentDate(model.getPaymentDate());
        dto.setUrlComprobante(model.getUrlComprobante());

        return dto;
    }

        public static void updateModel(Payment model, PaymentDTO dto) {
        if (model == null || dto == null) {
            return;
        }
        model.setDescription(dto.getDescription());
        model.setPaymentDate(dto.getPaymentDate());
    }
    public static Payment toModel(PaymentEntity entity) {
        if (entity == null) {
            return null;
        }

        Payment model = new Payment();
        model.setId(entity.getId());
        model.setStatus(entity.getStatus());
        model.setDescription(entity.getDescription());
        model.setPaymentDate(entity.getPaymentDate());

        return model;
    }

    public static VoucherMetadata getVoucherMetadata(PaymentEntity entity) {
        if (entity == null) {
            return null;
        }

        return new VoucherMetadata(
                entity.getVoucher(),
                entity.getVoucherType(),
                entity.getVoucherName(),
                entity.getVoucherSize()
        );
    }


    public static class VoucherMetadata {
        private final byte[] bytes;
        private final String type;
        private final String name;
        private final Long size;

        public VoucherMetadata(byte[] bytes, String type, String name, Long size) {
            this.bytes = bytes;
            this.type = type;
            this.name = name;
            this.size = size;
        }

        public byte[] getBytes() { return bytes; }
        public String getType() { return type; }
        public String getName() { return name; }
        public Long getSize() { return size; }
    }
}