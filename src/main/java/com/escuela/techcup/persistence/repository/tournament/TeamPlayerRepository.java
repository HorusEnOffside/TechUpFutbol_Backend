package com.escuela.techcup.persistence.repository.tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.escuela.techcup.persistence.entity.tournament.TeamPlayerEntity;

import java.util.List;
import java.util.Optional;

public interface TeamPlayerRepository extends JpaRepository<TeamPlayerEntity, String> {

    List<TeamPlayerEntity> findByTeamId(String teamId);

    Optional<TeamPlayerEntity> findByPlayerId(String playerId);

    boolean existsByPlayerId(String playerId);

    @Query("SELECT COUNT(tp) > 0 FROM TeamPlayerEntity tp WHERE tp.player.id = :playerId AND tp.team.tournament.id = :tournamentId")
    boolean existsByPlayerIdAndTournamentId(@Param("playerId") String playerId, @Param("tournamentId") String tournamentId);
}