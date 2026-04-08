package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.*;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.exception.TeamNotFoundException;
import com.escuela.techcup.core.service.TeamFullInfoService;
import com.escuela.techcup.persistence.entity.tournament.GoalEntity;
import com.escuela.techcup.persistence.entity.tournament.MatchEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamPlayerEntity;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.repository.tournament.GoalRepository;
import com.escuela.techcup.persistence.repository.tournament.MatchRepository;
import com.escuela.techcup.persistence.repository.tournament.TeamPlayerRepository;
import com.escuela.techcup.persistence.repository.tournament.TeamRepository;
import com.escuela.techcup.persistence.repository.tournament.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeamFullInfoServiceImpl implements TeamFullInfoService {

    private static final Logger log = LoggerFactory.getLogger(TeamFullInfoServiceImpl.class);

    private final TeamRepository teamRepository;
    private final TeamPlayerRepository teamPlayerRepository;
    private final MatchRepository matchRepository;
    private final GoalRepository goalRepository;
    private final TournamentRepository tournamentRepository;

    public TeamFullInfoServiceImpl(TeamRepository teamRepository,
                                   TeamPlayerRepository teamPlayerRepository,
                                   MatchRepository matchRepository,
                                   GoalRepository goalRepository,
                                   TournamentRepository tournamentRepository) {
        this.teamRepository = teamRepository;
        this.teamPlayerRepository = teamPlayerRepository;
        this.matchRepository = matchRepository;
        this.goalRepository = goalRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public TeamFullInfoDTO getTeamFullInfo(String teamId) {
        if (teamId == null || teamId.isBlank()) throw new InvalidInputException("teamId is required");

        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        return buildTeamFullInfo(team);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamFullInfoDTO> getTeamsByTournament(String tournamentId) {
        if (tournamentId == null || tournamentId.isBlank()) throw new InvalidInputException("tournamentId is required");

        tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new InvalidInputException("Tournament not found: " + tournamentId));

        return teamRepository.findAll().stream()
                .filter(t -> t.getTournament() != null && t.getTournament().getId().equals(tournamentId))
                .map(this::buildTeamFullInfo)
                .toList();
    }

    private TeamFullInfoDTO buildTeamFullInfo(TeamEntity team) {
        // Jugadores
        List<TeamPlayerEntity> teamPlayers = teamPlayerRepository.findByTeamId(team.getId());
        String captainPlayerId = team.getCaptainPlayer() != null ? team.getCaptainPlayer().getId() : null;

        List<TeamPlayerInfoDTO> playerDTOs = teamPlayers.stream()
                .map(tp -> {
                    PlayerEntity p = tp.getPlayer();
                    return new TeamPlayerInfoDTO(
                            p.getId(),
                            p.getUser().getName(),
                            p.getUser().getMail(),
                            p.getPosition(),
                            p.getDorsalNumber(),
                            p.getId().equals(captainPlayerId)
                    );
                }).toList();

        // Partidos
        List<MatchEntity> matches = matchRepository.findByTeamAIdOrTeamBId(team.getId(), team.getId());
        LocalDateTime now = LocalDateTime.now();

        List<TeamMatchInfoDTO> matchDTOs = matches.stream().map(match -> {
            boolean isTeamA = match.getTeamA().getId().equals(team.getId());
            String opponentName = isTeamA ? match.getTeamB().getName() : match.getTeamA().getName();

            // Calcular goles desde GoalEntity
            List<GoalEntity> goals = goalRepository.findByMatchId(match.getId());

            int goalsFor = 0;
            int goalsAgainst = 0;

            for (GoalEntity goal : goals) {
                String scorerTeamId = goal.getPlayer().getTeam() != null
                        ? goal.getPlayer().getTeam().getId() : null;
                if (team.getId().equals(scorerTeamId)) goalsFor++;
                else goalsAgainst++;
            }

            String result = "PENDING";
            if (match.getDateTime().isBefore(now)) {
                if (goalsFor > goalsAgainst) result = "WIN";
                else if (goalsFor < goalsAgainst) result = "LOSS";
                else result = "DRAW";
            }

            return new TeamMatchInfoDTO(
                    match.getId(),
                    match.getDateTime(),
                    opponentName,
                    goalsFor,
                    goalsAgainst,
                    result
            );
        }).toList();

        // Estadísticas
        int played = (int) matchDTOs.stream().filter(m -> !m.getResult().equals("PENDING")).count();
        int wins = (int) matchDTOs.stream().filter(m -> m.getResult().equals("WIN")).count();
        int losses = (int) matchDTOs.stream().filter(m -> m.getResult().equals("LOSS")).count();
        int draws = (int) matchDTOs.stream().filter(m -> m.getResult().equals("DRAW")).count();
        int goalsFor = matchDTOs.stream().mapToInt(TeamMatchInfoDTO::getGoalsFor).sum();
        int goalsAgainst = matchDTOs.stream().mapToInt(TeamMatchInfoDTO::getGoalsAgainst).sum();

        TeamStatsDTO stats = new TeamStatsDTO(played, wins, losses, draws, goalsFor, goalsAgainst);

        // Torneo
        String tournamentId = team.getTournament() != null ? team.getTournament().getId() : null;
        String tournamentName = null;
        if (team.getTournament() != null) {
            tournamentName = "Torneo " + team.getTournament().getId().substring(0, 8);
        }

        // Capitán
        String captainName = team.getCaptainPlayer() != null
                ? team.getCaptainPlayer().getUser().getName() : null;

        log.info("Team full info built. teamId={}, players={}, matches={}", team.getId(), playerDTOs.size(), matchDTOs.size());

        return new TeamFullInfoDTO(
                team.getId(),
                team.getName(),
                team.getUniformColor(),
                tournamentId,
                tournamentName,
                captainName,
                playerDTOs,
                matchDTOs,
                stats
        );
    }
}