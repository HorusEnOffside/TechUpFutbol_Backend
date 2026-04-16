package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.PaymentDTO;
import com.escuela.techcup.core.exception.InvalidImageException;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.model.enums.PaymentStatus;
import com.escuela.techcup.core.service.PaymentService;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
import com.escuela.techcup.persistence.mapper.payment.PaymentMapper;
import com.escuela.techcup.persistence.repository.payment.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public Payment createPayment(PaymentDTO paymentDTO, MultipartFile file) {
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
        return paymentRepository.findAll()
                .stream()
                .map(PaymentMapper::toModel)
                .collect(Collectors.toList());
    }



    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentById(String id) {
        PaymentEntity entity = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con id: " + id));
        return PaymentMapper.toModel(entity);
    }


    @Override
    @Transactional
    public Payment updatePaymentState(String id, PaymentStatus paymentStatus) {
        PaymentEntity entity = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con id: " + id));

        PaymentMapper.updateStatus(entity, paymentStatus);

        PaymentEntity updatedEntity = paymentRepository.save(entity);
        return PaymentMapper.toModel(updatedEntity);
    }

    @Override
    @Transactional
    public void deletePayment(String id) {
        if (!paymentRepository.existsById(id)) {
            throw new EntityNotFoundException("Pago no encontrado con id: " + id);
        }
        paymentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentEntity getVoucherById(String id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        if (status == null) {
            throw new InvalidInputException("Status is required");
        }

        List<PaymentEntity> paymentEntities = paymentRepository.findByStatus(status);

        return paymentEntities.stream()
                .map(PaymentMapper::toModel)
                .collect(Collectors.toList());
    }
}