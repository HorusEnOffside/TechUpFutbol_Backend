package com.escuela.techcup.persistence.repository.tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import com.escuela.techcup.persistence.entity.tournament.MatchEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<MatchEntity, String> {

    List<MatchEntity> findByDateTimeBetween(LocalDateTime from, LocalDateTime to);

    List<MatchEntity> findByRefereeId(String refereeId);

    List<MatchEntity> findBySoccerFieldId(String soccerFieldId);

    List<MatchEntity> findByTeamAIdOrTeamBId(String teamAId, String teamBId);

    boolean existsByDateTimeAndTeamAIdOrDateTimeAndTeamBId(
            LocalDateTime dt1, String teamAId,
            LocalDateTime dt2, String teamBId);

    boolean existsByDateTimeAndSoccerFieldId(LocalDateTime dateTime, String soccerFieldId);

    @Query("SELECT m FROM MatchEntity m WHERE m.id = :matchId " +
           "AND (m.teamA.id = :teamId OR m.teamB.id = :teamId)")
    Optional<MatchEntity> findByIdAndTeam(@Param("matchId") String matchId,
                                          @Param("teamId") String teamId);
}