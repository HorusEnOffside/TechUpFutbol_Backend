package com.escuela.techcup.persistence.repository.payment;

import com.escuela.techcup.core.model.enums.PaymentStatus;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {

    List<PaymentEntity> findByStatus(PaymentStatus status);
}