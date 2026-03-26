package com.escuela.techcup.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.users.FamiliarEntity;

import java.util.Optional;
import java.util.UUID;

public interface FamiliarRepository extends JpaRepository<FamiliarEntity, UUID> {
    Optional<FamiliarEntity> findByMailIgnoreCase(String mail);
    boolean existsByMailIgnoreCase(String mail);
}