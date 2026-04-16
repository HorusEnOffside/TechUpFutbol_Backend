package com.escuela.techcup.persistence.mapper.payment;

import com.escuela.techcup.controller.dto.PaymentDTO;
import com.escuela.techcup.controller.dto.PaymentRespondDTO;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.model.enums.PaymentStatus;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
public class PaymentMapper {

    private static String baseUrl;

    @Value("${app.base-url:http://localhost:8080}")
    public void setBaseUrl(String url) {
        PaymentMapper.baseUrl = url;
    }


    public static PaymentEntity toEntity(PaymentDTO paymentDTO, MultipartFile voucher) {
        if (paymentDTO == null) return null;

        PaymentEntity entity = new PaymentEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setStatus(PaymentStatus.PENDING);
        entity.setDescription(paymentDTO.getDescription());
        entity.setPaymentDate(paymentDTO.getPaymentDate());

        if (voucher != null && !voucher.isEmpty()) {
            try {
                entity.setVoucher(voucher.getBytes());
                entity.setVoucherName(voucher.getOriginalFilename());
                entity.setVoucherType(voucher.getContentType());
            } catch (IOException e) {
                throw new RuntimeException("Error al procesar el archivo del comprobante", e);
            }
        }

        return entity;
    }

    public static PaymentRespondDTO toRespondDTO(Payment entity) {
        if (entity == null) return null;

        PaymentRespondDTO respondDTO = new PaymentRespondDTO();
        respondDTO.setId(entity.getId());
        respondDTO.setStatus(entity.getStatus());
        respondDTO.setDescription(entity.getDescription());
        respondDTO.setPaymentDate(entity.getPaymentDate());
        respondDTO.setUrlComprobante(entity.getVoucherURL());

        return respondDTO;
    }

    private static String buildVoucherUrl(String paymentId) {
        if (paymentId == null || baseUrl == null) return null;

        String normalizedBaseUrl = baseUrl.endsWith("/")
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;

        return String.format("%s/api/payments/%s/voucher", normalizedBaseUrl, paymentId);
    }

    public static void updateEntity(PaymentEntity existingEntity, PaymentDTO paymentDTO, MultipartFile newVoucher) {
        if (existingEntity == null || paymentDTO == null) return;

        if (paymentDTO.getDescription() != null)
            existingEntity.setDescription(paymentDTO.getDescription());

        if (paymentDTO.getPaymentDate() != null)
            existingEntity.setPaymentDate(paymentDTO.getPaymentDate());

        if (newVoucher != null && !newVoucher.isEmpty()) {
            try {
                existingEntity.setVoucher(newVoucher.getBytes());
                existingEntity.setVoucherName(newVoucher.getOriginalFilename());
                existingEntity.setVoucherType(newVoucher.getContentType());
            } catch (IOException e) {
                throw new RuntimeException("Error al actualizar el archivo del comprobante", e);
            }
        }
    }

    public static void updateStatus(PaymentEntity entity, PaymentStatus status) {
        if (entity != null && status != null)
            entity.setStatus(status);
    }

    public static Payment toModel(PaymentEntity entity) {
        if (entity == null) return null;

        Payment payment = new Payment();
        payment.setId(entity.getId());
        payment.setStatus(entity.getStatus());
        payment.setDescription(entity.getDescription());
        payment.setPaymentDate(entity.getPaymentDate());
        payment.setVoucherURL(buildVoucherUrl(entity.getId()));

        return payment;
    }
}