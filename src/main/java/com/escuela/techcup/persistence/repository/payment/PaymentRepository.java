package com.escuela.techcup.persistence.repository.payment;

import java.util.List;

import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.core.model.enums.PaymentStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {

    List<PaymentEntity> findByStatus(PaymentStatus status);

}