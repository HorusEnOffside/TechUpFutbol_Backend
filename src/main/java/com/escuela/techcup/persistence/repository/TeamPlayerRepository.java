package com.escuela.techcup.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.tournament.TeamPlayerEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamPlayerRepository extends JpaRepository<TeamPlayerEntity, UUID> {

    List<TeamPlayerEntity> findByTeamId(UUID teamId);

    Optional<TeamPlayerEntity> findByPlayerId(UUID playerId);

    boolean existsByPlayerId(UUID playerId);
}