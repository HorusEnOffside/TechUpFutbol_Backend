package com.escuela.techcup.persistence.repository;

import com.escuela.techcup.persistence.entity.NormalPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NormalPaymentRepository extends JpaRepository<NormalPaymentEntity, UUID> {
}