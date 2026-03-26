package com.escuela.techcup.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.payment.NormalPaymentEntity;

import java.util.UUID;

public interface NormalPaymentRepository extends JpaRepository<NormalPaymentEntity, UUID> {
}