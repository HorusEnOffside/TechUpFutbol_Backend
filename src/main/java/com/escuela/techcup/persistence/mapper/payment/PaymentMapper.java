package com.escuela.techcup.persistence.mapper.payment;
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
}