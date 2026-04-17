package com.escuela.techcup.persistence.repository.tournament;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.tournament.GoalEntity;

import java.util.List;

public interface GoalRepository extends JpaRepository<GoalEntity, UUID> {

    List<GoalEntity> findByMatchId(UUID matchId);

    List<GoalEntity> findByPlayerId(UUID playerId);
}