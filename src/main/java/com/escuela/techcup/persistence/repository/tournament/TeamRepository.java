package com.escuela.techcup.persistence.repository.tournament;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.tournament.TeamEntity;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<TeamEntity, UUID> {

    Optional<TeamEntity> findByNameIgnoreCase(String name);
    Optional<TeamEntity> findById(String id);
    List<TeamEntity> findByNameContainingIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}