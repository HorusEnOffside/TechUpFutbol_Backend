package com.escuela.techcup.persistence.repository;

import com.escuela.techcup.persistence.entity.RefereeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefereeRepository extends JpaRepository<RefereeEntity, UUID> {
    Optional<RefereeEntity> findByMailIgnoreCase(String mail);
    boolean existsByMailIgnoreCase(String mail);
}