package com.escuela.techcup.persistence.repository;

import com.escuela.techcup.persistence.entity.AdministratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdministratorRepository extends JpaRepository<AdministratorEntity, UUID> {
    Optional<AdministratorEntity> findByMailIgnoreCase(String mail);
    boolean existsByMailIgnoreCase(String mail);
}