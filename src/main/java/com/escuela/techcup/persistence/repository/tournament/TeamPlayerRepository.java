package com.escuela.techcup.persistence.repository.tournament;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.tournament.TeamPlayerEntity;

import java.util.List;
import java.util.Optional;

public interface TeamPlayerRepository extends JpaRepository<TeamPlayerEntity, String> {

    List<TeamPlayerEntity> findByTeamId(String teamId);

    Optional<TeamPlayerEntity> findByPlayerId(String playerId);

    boolean existsByPlayerId(String playerId);
}