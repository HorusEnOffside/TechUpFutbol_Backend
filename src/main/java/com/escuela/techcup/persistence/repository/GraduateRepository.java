package com.escuela.techcup.persistence.repository;

import com.escuela.techcup.persistence.entity.GraduateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GraduateRepository extends JpaRepository<GraduateEntity, UUID> {
    Optional<GraduateEntity> findByMailIgnoreCase(String mail);
    boolean existsByMailIgnoreCase(String mail);
}