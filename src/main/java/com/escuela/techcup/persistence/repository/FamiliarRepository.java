package com.escuela.techcup.persistence.repository;

import com.escuela.techcup.persistence.entity.FamiliarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FamiliarRepository extends JpaRepository<FamiliarEntity, UUID> {
    Optional<FamiliarEntity> findByMailIgnoreCase(String mail);
    boolean existsByMailIgnoreCase(String mail);
}