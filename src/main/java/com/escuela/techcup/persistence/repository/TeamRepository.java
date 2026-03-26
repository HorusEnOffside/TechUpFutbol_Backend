package com.escuela.techcup.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.tournament.TeamEntity;

import java.util.Optional;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<TeamEntity, UUID> {

    Optional<TeamEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}