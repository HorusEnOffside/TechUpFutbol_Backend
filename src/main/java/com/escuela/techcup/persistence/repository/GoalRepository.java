package com.escuela.techcup.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.tournament.GoalEntity;

import java.util.List;
import java.util.UUID;

public interface GoalRepository extends JpaRepository<GoalEntity, UUID> {

    List<GoalEntity> findByMatchId(UUID matchId);

    List<GoalEntity> findByPlayerId(UUID playerId);
}