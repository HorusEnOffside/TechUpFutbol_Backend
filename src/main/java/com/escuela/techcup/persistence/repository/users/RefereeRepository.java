package com.escuela.techcup.persistence.repository.users;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.users.RefereeEntity;

import java.util.Optional;

public interface RefereeRepository extends JpaRepository<RefereeEntity, UUID> {
    Optional<RefereeEntity> findByMailIgnoreCase(String mail);
    boolean existsByMailIgnoreCase(String mail);
}