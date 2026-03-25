package com.escuela.techcup.persistence.repository;

import com.escuela.techcup.persistence.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<TeamEntity, UUID> {

    Optional<TeamEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}