package com.escuela.techcup.persistence.repository.users;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.users.GraduateEntity;

import java.util.Optional;

public interface GraduateRepository extends JpaRepository<GraduateEntity, UUID> {
    Optional<GraduateEntity> findByMailIgnoreCase(String mail);
    boolean existsByMailIgnoreCase(String mail);
}