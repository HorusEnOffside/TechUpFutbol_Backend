package com.escuela.techcup.persistence.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.payment.NormalPaymentEntity;


public interface NormalPaymentRepository extends JpaRepository<NormalPaymentEntity, String> {
}