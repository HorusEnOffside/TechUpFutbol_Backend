package com.escuela.techcup.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.users.OrganizerEntity;

import java.util.Optional;
import java.util.UUID;

public interface OrganizerRepository extends JpaRepository<OrganizerEntity, UUID> {
    Optional<OrganizerEntity> findByMailIgnoreCase(String mail);
    boolean existsByMailIgnoreCase(String mail);
}