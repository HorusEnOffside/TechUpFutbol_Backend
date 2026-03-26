package com.escuela.techcup.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.users.GraduateEntity;

import java.util.Optional;
import java.util.UUID;

public interface GraduateRepository extends JpaRepository<GraduateEntity, UUID> {
    Optional<GraduateEntity> findByMailIgnoreCase(String mail);
    boolean existsByMailIgnoreCase(String mail);
}