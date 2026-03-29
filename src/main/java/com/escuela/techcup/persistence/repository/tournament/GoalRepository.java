package com.escuela.techcup.persistence.repository.tournament;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.tournament.GoalEntity;

import java.util.List;

public interface GoalRepository extends JpaRepository<GoalEntity, String> {

    List<GoalEntity> findByMatchId(String matchId);

    List<GoalEntity> findByPlayerId(String playerId);
}