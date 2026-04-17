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
import java.util.UUID;

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
        TeamEntity team = teamRepository.findById(UUID.fromString(teamId))
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        return buildTeamFullInfo(team);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamFullInfoDTO> getTeamsByTournament(String tournamentId) {
        if (tournamentId == null || tournamentId.isBlank()) throw new InvalidInputException("tournamentId is required");
        tournamentRepository.findById(UUID.fromString(tournamentId))
                .orElseThrow(() -> new InvalidInputException("Tournament not found: " + tournamentId));
        return teamRepository.findAll().stream()
                .filter(t -> t.getTournament() != null && UUID.fromString(tournamentId).equals(t.getTournament().getId()))
                .map(this::buildTeamFullInfo)
                .toList();
    }

    // ── Método principal dividido en submétodos para reducir complejidad cognitiva ──

    private TeamFullInfoDTO buildTeamFullInfo(TeamEntity team) {
        List<TeamPlayerEntity> teamPlayers = teamPlayerRepository.findByTeamId(team.getId());
        String captainPlayerId = resolveCaptainId(team);  // already String via toString()

        List<TeamPlayerInfoDTO> playerDTOs = buildPlayerDTOs(teamPlayers, captainPlayerId);
        List<TeamMatchInfoDTO> matchDTOs = buildMatchDTOs(team);
        TeamStatsDTO stats = buildStats(matchDTOs);

        String tournamentId = resolveTournamentId(team);
        String tournamentName = resolveTournamentName(team);
        String captainName = resolveCaptainName(team);

        log.info("Team full info built. teamId={}, players={}, matches={}",
                team.getId(), playerDTOs.size(), matchDTOs.size());

        return new TeamFullInfoDTO(
                team.getId() != null ? team.getId().toString() : null,
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

    private String resolveCaptainId(TeamEntity team) {
        return team.getCaptainPlayer() != null && team.getCaptainPlayer().getId() != null
                ? team.getCaptainPlayer().getId().toString() : null;
    }

    private String resolveCaptainName(TeamEntity team) {
        return team.getCaptainPlayer() != null ? team.getCaptainPlayer().getUser().getName() : null;
    }

    private String resolveTournamentId(TeamEntity team) {
        return team.getTournament() != null && team.getTournament().getId() != null
                ? team.getTournament().getId().toString() : null;
    }

    private String resolveTournamentName(TeamEntity team) {
        if (team.getTournament() == null || team.getTournament().getId() == null) return null;
        String tourId = team.getTournament().getId().toString();
        return "Torneo " + tourId.substring(0, Math.min(8, tourId.length()));
    }

    private List<TeamPlayerInfoDTO> buildPlayerDTOs(List<TeamPlayerEntity> teamPlayers, String captainPlayerId) {
        return teamPlayers.stream()
                .map(tp -> toPlayerInfoDTO(tp.getPlayer(), captainPlayerId))
                .toList();
    }

    private TeamPlayerInfoDTO toPlayerInfoDTO(PlayerEntity p, String captainPlayerId) {
        String playerId = p.getId() != null ? p.getId().toString() : null;
        return new TeamPlayerInfoDTO(
                playerId,
                p.getUser().getName(),
                p.getUser().getMail(),
                p.getPosition(),
                p.getDorsalNumber(),
                playerId != null && playerId.equals(captainPlayerId)
        );
    }

    private List<TeamMatchInfoDTO> buildMatchDTOs(TeamEntity team) {
        List<MatchEntity> matches = matchRepository.findByTeamAIdOrTeamBId(team.getId(), team.getId());
        LocalDateTime now = LocalDateTime.now();
        UUID teamUUID = team.getId();
        return matches.stream()
                .map(match -> toMatchInfoDTO(match, teamUUID, now))
                .toList();
    }

    private TeamMatchInfoDTO toMatchInfoDTO(MatchEntity match, UUID teamId, LocalDateTime now) {
        boolean isTeamA = match.getTeamA().getId().equals(teamId);
        String opponentName = isTeamA ? match.getTeamB().getName() : match.getTeamA().getName();

        int[] goals = countGoals(match, teamId);
        int goalsFor = goals[0];
        int goalsAgainst = goals[1];

        String result = resolveMatchResult(match.getDateTime(), now, goalsFor, goalsAgainst);

        return new TeamMatchInfoDTO(
                match.getId() != null ? match.getId().toString() : null,
                match.getDateTime(), opponentName, goalsFor, goalsAgainst, result);
    }

    private int[] countGoals(MatchEntity match, UUID teamId) {
        List<GoalEntity> goals = goalRepository.findByMatchId(match.getId());
        int goalsFor = 0;
        int goalsAgainst = 0;
        for (GoalEntity goal : goals) {
            UUID scorerTeamId = goal.getPlayer().getTeam() != null
                    ? goal.getPlayer().getTeam().getId() : null;
            if (teamId.equals(scorerTeamId)) goalsFor++;
            else goalsAgainst++;
        }
        return new int[]{goalsFor, goalsAgainst};
    }

    private String resolveMatchResult(LocalDateTime matchTime, LocalDateTime now, int goalsFor, int goalsAgainst) {
        if (matchTime.isAfter(now)) return "PENDING";
        if (goalsFor > goalsAgainst) return "WIN";
        if (goalsFor < goalsAgainst) return "LOSS";
        return "DRAW";
    }

    private TeamStatsDTO buildStats(List<TeamMatchInfoDTO> matchDTOs) {
        int played   = (int) matchDTOs.stream().filter(m -> !m.getResult().equals("PENDING")).count();
        int wins     = (int) matchDTOs.stream().filter(m -> m.getResult().equals("WIN")).count();
        int losses   = (int) matchDTOs.stream().filter(m -> m.getResult().equals("LOSS")).count();
        int draws    = (int) matchDTOs.stream().filter(m -> m.getResult().equals("DRAW")).count();
        int goalsFor     = matchDTOs.stream().mapToInt(TeamMatchInfoDTO::getGoalsFor).sum();
        int goalsAgainst = matchDTOs.stream().mapToInt(TeamMatchInfoDTO::getGoalsAgainst).sum();
        return new TeamStatsDTO(played, wins, losses, draws, goalsFor, goalsAgainst);
    }
}