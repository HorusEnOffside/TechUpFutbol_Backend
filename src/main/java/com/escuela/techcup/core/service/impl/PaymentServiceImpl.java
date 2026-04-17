package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.PaymentDTO;
import com.escuela.techcup.core.exception.InvalidImageException;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.exception.TechcupException;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.model.enums.PaymentStatus;
import com.escuela.techcup.core.service.PaymentService;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
import com.escuela.techcup.persistence.mapper.payment.PaymentMapper;
import com.escuela.techcup.persistence.repository.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private static final String PAYMENT_NOT_FOUND = "Pago no encontrado con id: ";

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public Payment createPayment(PaymentDTO paymentDTO, MultipartFile file) {
        log.info("Creating payment. description={}", paymentDTO.getDescription());
        if (file == null || file.isEmpty()) {
            throw new InvalidImageException("El comprobante de pago es obligatorio");
        }
        PaymentEntity entity = PaymentMapper.toEntity(paymentDTO, file);
        PaymentEntity savedEntity = paymentRepository.save(entity);
        return PaymentMapper.toModel(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPayments() {
        log.info("Fetching all payments");
        return paymentRepository.findAll()
                .stream()
                .map(PaymentMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentById(String id) {
        log.info("Fetching payment by id={}", id);
        PaymentEntity entity = paymentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new TechcupException(PAYMENT_NOT_FOUND + id, HttpStatus.NOT_FOUND));
        return PaymentMapper.toModel(entity);
    }

    @Override
    @Transactional
    public Payment updatePaymentState(String id, PaymentStatus paymentStatus) {
        log.info("Updating payment id={} to status={}", id, paymentStatus);
        PaymentEntity entity = paymentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new TechcupException(PAYMENT_NOT_FOUND + id, HttpStatus.NOT_FOUND));
        PaymentMapper.updateStatus(entity, paymentStatus);
        PaymentEntity updatedEntity = paymentRepository.save(entity);
        return PaymentMapper.toModel(updatedEntity);
    }

    @Override
    @Transactional
    public void deletePayment(String id) {
        log.info("Deleting payment id={}", id);
        if (!paymentRepository.existsById(UUID.fromString(id))) {
            throw new TechcupException(PAYMENT_NOT_FOUND + id, HttpStatus.NOT_FOUND);
        }
        paymentRepository.deleteById(UUID.fromString(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentEntity getVoucherById(String id) {
        log.info("Fetching voucher for payment id={}", id);
        return paymentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new TechcupException(PAYMENT_NOT_FOUND + id, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        log.info("Fetching payments by status={}", status);
        if (status == null) {
            throw new InvalidInputException("Status is required");
        }
        List<PaymentEntity> paymentEntities = paymentRepository.findByStatus(status);
        return paymentEntities.stream()
                .map(PaymentMapper::toModel)
                .collect(Collectors.toList());
    }
}
