package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.PaymentDTO;
import com.escuela.techcup.core.exception.FileStorageException;
import com.escuela.techcup.core.exception.InvalidFileException;
import com.escuela.techcup.core.exception.PaymentNotFoundException;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.model.enums.PaymentStatus;
import com.escuela.techcup.core.service.PaymentService;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
import com.escuela.techcup.persistence.mapper.payment.PaymentMapper;
import com.escuela.techcup.persistence.repository.payment.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {

        this.paymentRepository = paymentRepository;
    }


    @Override
    public Payment createPayment(PaymentDTO paymentDTO, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("El comprobante es obligatorio");
        }

        String voucherType = file.getContentType();
        if (!isValidFileType(voucherType)) {
            throw new InvalidFileException(
                    "Tipo de archivo no permitido: %s. Solo se permiten: JPG, PNG, PDF");
        }

        Payment model = PaymentMapper.toModel(paymentDTO);
        model.setId(UUID.randomUUID().toString());
        model.setStatus(PaymentStatus.PENDING);
        if (model.getPaymentDate() == null) {
            model.setPaymentDate(LocalDateTime.now());
        }

        byte[] voucherBytes;
        String voucherName;
        Long voucherSize;

        try {
            voucherBytes = file.getBytes();
            voucherName = file.getOriginalFilename();
            voucherSize = file.getSize();
        } catch (IOException e) {
            throw new FileStorageException("Error al leer el archivo: " + e.getMessage());
        }

        PaymentEntity entity = PaymentMapper.toEntity(
                model,
                voucherBytes,
                voucherType,
                voucherName,
                voucherSize
        );

        PaymentEntity savedEntity = paymentRepository.save(entity);

        String voucherUrl = generarVoucherUrl(savedEntity.getId());

        return PaymentMapper.toModel(savedEntity, voucherUrl);

    }

    @Override
    public List<Payment> getPayments() {

        List<PaymentEntity> entities = paymentRepository.findAll();

        return entities.stream()
                .map(entity -> {
                    String voucherUrl = generarVoucherUrl(entity.getId());
                    return PaymentMapper.toModel(entity, voucherUrl);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Payment getPaymentById(String id) {

        PaymentEntity entity = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Pago no encontrado con ID: " + id));

        String voucherUrl = generarVoucherUrl(entity.getId());
        return PaymentMapper.toModel(entity, voucherUrl);
    }

    @Override
    public void updatePaymentState(String id, PaymentStatus paymentStatus) {
        PaymentEntity entity = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Pago no encontrado con ID: " + id));

        entity.setStatus(paymentStatus);
        paymentRepository.save(entity);
    }

    @Override
    public void deletePayment(String id) {
        PaymentEntity entity = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Pago no encontrado con ID: " + id));
        paymentRepository.delete(entity);
    }


    private boolean isValidFileType(String contentType) {
        if (contentType == null) return false;
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("application/pdf");
    }

    private String generarVoucherUrl(String paymentId) {
        return "/api/payments/" + paymentId + "/voucher";
    }
}
