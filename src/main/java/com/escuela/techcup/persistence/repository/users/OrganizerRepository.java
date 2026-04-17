package com.escuela.techcup.persistence.repository.users;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.users.OrganizerEntity;

import java.util.Optional;

public interface OrganizerRepository extends JpaRepository<OrganizerEntity, UUID> {
    Optional<OrganizerEntity> findByMailIgnoreCase(String mail);
    boolean existsByMailIgnoreCase(String mail);
}