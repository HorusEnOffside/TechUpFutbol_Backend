package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.service.PaymentService;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
import com.escuela.techcup.persistence.mapper.payment.PaymentMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Override
    public Payment createPayment(Payment payment, MultipartFile file) {
        log.info("Creating payment for date: {}", payment.getPaymentDate());

        PaymentEntity entity = PaymentMapper.toEntity(
                payment,
//                file.getBytes(),
                file.getContentType(),
                file.getOriginalFilename(),
                file.getSize()
        );

        PaymentEntity saved = repository.save(entity);

        String url = generarUrl(saved.getId());
        return PaymentMapper.toModel(saved, url);
    }

    @Override
    public List<Payment> getPayments() {
        return List.of();
    }

    @Override
    public Payment getPaymentById(int id) {
        return null;
    }

    @Override
    public void updatePayment(Payment payment) {

    }

    @Override
    public void deletePayment(int id) {

    }
}
