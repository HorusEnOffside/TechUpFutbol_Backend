package com.escuela.techcup.persistence.repository.payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.core.model.enums.PaymentStatus;


public interface NormalPaymentRepository extends JpaRepository<NormalPaymentEntity, String> {

    List<PaymentEntity> findByStatus(PaymentStatus status);
}