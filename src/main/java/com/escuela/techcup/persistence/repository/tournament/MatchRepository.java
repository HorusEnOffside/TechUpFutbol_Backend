package com.escuela.techcup.persistence.repository.tournament;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.escuela.techcup.persistence.entity.tournament.MatchEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<MatchEntity, UUID> {

    List<MatchEntity> findByDateTimeBetween(LocalDateTime from, LocalDateTime to);

    List<MatchEntity> findByRefereeId(UUID refereeId);

    List<MatchEntity> findBySoccerFieldId(UUID soccerFieldId);

    List<MatchEntity> findByTeamAIdOrTeamBId(UUID teamAId, UUID teamBId);

    boolean existsByDateTimeAndTeamAIdOrDateTimeAndTeamBId(
            LocalDateTime dt1, UUID teamAId,
            LocalDateTime dt2, UUID teamBId);

    boolean existsByDateTimeAndSoccerFieldId(LocalDateTime dateTime, UUID soccerFieldId);

    @Query("SELECT m FROM MatchEntity m WHERE m.id = :matchId " +
           "AND (m.teamA.id = :teamId OR m.teamB.id = :teamId)")
    Optional<MatchEntity> findByIdAndTeam(@Param("matchId") UUID matchId,
                                          @Param("teamId") UUID teamId);
}