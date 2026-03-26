package com.escuela.techcup.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.tournament.MatchEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<MatchEntity, UUID> {

    List<MatchEntity> findByDateTimeBetween(LocalDateTime from, LocalDateTime to);

    List<MatchEntity> findByRefereeId(UUID refereeId);

    List<MatchEntity> findBySoccerFieldId(UUID soccerFieldId);

    List<MatchEntity> findByTeamAIdOrTeamBId(UUID teamAId, UUID teamBId);
}