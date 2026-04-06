package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.exception.*;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.enums.Formation;
import com.escuela.techcup.core.model.enums.InvitationStatus;
import com.escuela.techcup.core.service.TeamService;
import com.escuela.techcup.core.util.DateUtil;
import com.escuela.techcup.core.util.IdGeneratorUtil;
import com.escuela.techcup.persistence.entity.tournament.InvitationEntity;
import com.escuela.techcup.persistence.entity.tournament.MatchEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamPlayerEntity;
import com.escuela.techcup.persistence.entity.users.GraduateEntity;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.StudentEntity;
import com.escuela.techcup.persistence.entity.users.TeacherEntity;
import com.escuela.techcup.persistence.mapper.TeamMapper;
import com.escuela.techcup.persistence.repository.tournament.InvitationRepository;
import com.escuela.techcup.persistence.repository.tournament.MatchRepository;
import com.escuela.techcup.persistence.repository.tournament.TeamPlayerRepository;
import com.escuela.techcup.persistence.repository.tournament.TeamRepository;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;
    private final TeamPlayerRepository teamPlayerRepository;
    private final PlayerRepository playerRepository;
    private final InvitationRepository invitationRepository;
    private final MatchRepository matchRepository;

    public TeamServiceImpl(
            TeamRepository teamRepository,
            TeamPlayerRepository teamPlayerRepository,
            PlayerRepository playerRepository,
            InvitationRepository invitationRepository,
            MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.teamPlayerRepository = teamPlayerRepository;
        this.playerRepository = playerRepository;
        this.invitationRepository = invitationRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    @Transactional
    public Team createTeam(String name, String uniformColors, BufferedImage logo, String captainUserId) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("Team name is required");
        }
        if (name.length() < 5) {
            throw new InvalidInputException("Team name must have at least 5 characters");
        }
        if (uniformColors == null || uniformColors.isBlank()) {
            throw new InvalidInputException("Uniform colors are required");
        }
        if (teamRepository.existsByNameIgnoreCase(name)) {
            throw new TeamAlreadyExistsException(name);
        }

        String teamId = IdGeneratorUtil.generateId();
        Team team = new Team(teamId, name, uniformColors, logo, null);

        TeamEntity entity = TeamMapper.toEntity(team);
        teamRepository.save(entity);

        log.info("Team created successfully. teamId={}, name={}", teamId, name);
        return team;
    }

    @Override
    @Transactional
    public void invitePlayer(String teamId, String playerId, String message) {
        if (teamId == null || teamId.isBlank()) {
            throw new InvalidInputException("teamId is required");
        }
        if (playerId == null || playerId.isBlank()) {
            throw new InvalidInputException("playerId is required");
        }

        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        PlayerEntity player = playerRepository.findById(playerId)
                .orElseThrow(() -> new InvalidInputException("Player not found"));

        if (invitationRepository.existsByTeamIdAndPlayerId(teamId, playerId)) {
            throw new PlayerAlreadyInvitedException(playerId);
        }

        InvitationEntity invitation = new InvitationEntity();
        invitation.setId(IdGeneratorUtil.generateId());
        invitation.setTeam(team);
        invitation.setPlayer(player);
        invitation.setMessage(message);
        invitation.setStatus(InvitationStatus.PENDING);

        invitationRepository.save(invitation);
        log.info("Invitation sent. teamId={}, playerId={}", teamId, playerId);
    }

    @Override
    @Transactional
    public void respondInvitation(String invitationId, InvitationStatus action) {
        if (invitationId == null || invitationId.isBlank()) {
            throw new InvalidInputException("invitationId is required");
        }
        if (action == null) {
            throw new InvalidInputException("action is required");
        }

        InvitationEntity invitation = invitationRepository
                .findByIdAndStatus(invitationId, InvitationStatus.PENDING)
                .orElseThrow(() -> new InvitationNotFoundException(invitationId));

        invitation.setStatus(action);
        invitationRepository.save(invitation);

        if (action == InvitationStatus.ACCEPTED) {
            TeamPlayerEntity teamPlayer = new TeamPlayerEntity();
            teamPlayer.setId(IdGeneratorUtil.generateId());
            teamPlayer.setTeam(invitation.getTeam());
            teamPlayer.setPlayer(invitation.getPlayer());
            teamPlayerRepository.save(teamPlayer);
            log.info("Player accepted invitation. invitationId={}", invitationId);
        } else {
            log.info("Player rejected invitation. invitationId={}", invitationId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateTeamComposition(String teamId) {
        if (teamId == null || teamId.isBlank()) {
            throw new InvalidInputException("teamId is required");
        }

        List<TeamPlayerEntity> players = teamPlayerRepository.findByTeamId(teamId);
        int count = players.size();
        boolean valid = count >= 7 && count <= 12;

        log.info("Team composition validation. teamId={}, players={}, valid={}", teamId, count, valid);
        return valid;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validatePlayerUniquePerTournament(String playerId, String tournamentId) {
        if (playerId == null || playerId.isBlank()) {
            throw new InvalidInputException("playerId is required");
        }
        if (tournamentId == null || tournamentId.isBlank()) {
            throw new InvalidInputException("tournamentId is required");
        }

        boolean exists = teamPlayerRepository.existsByPlayerIdAndTournamentId(playerId, tournamentId);
        log.info("Player unique validation. playerId={}, tournamentId={}, exists={}", playerId, tournamentId, exists);
        return !exists;
    }

    @Override
    @Transactional(readOnly = true)
    public Team getTeamById(String teamId) {
        if (teamId == null || teamId.isBlank()) {
            throw new InvalidInputException("teamId is required");
        }
        return teamRepository.findById(teamId)
                .map(TeamMapper::toModel)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(TeamMapper::toModel)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateEngineeringMajority(String teamId) {
        if (teamId == null || teamId.isBlank()) {
            throw new InvalidInputException("teamId is required");
        }

        List<TeamPlayerEntity> teamPlayers = teamPlayerRepository.findByTeamId(teamId);

        if (teamPlayers.isEmpty()) {
            throw new InvalidInputException("Team has no players");
        }

        long engineeringCount = teamPlayers.stream()
                .map(TeamPlayerEntity::getPlayer)
                .map(PlayerEntity::getUser)
                .filter(user ->
                        user instanceof StudentEntity ||
                                user instanceof TeacherEntity ||
                                user instanceof GraduateEntity
                )
                .count();

        int total = teamPlayers.size();
        boolean valid = engineeringCount > total / 2.0;

        log.info("Engineering majority validation. teamId={}, total={}, engineering={}, valid={}",
                teamId, total, engineeringCount, valid);

        return valid;
    }

    @Override
    @Transactional
    public void changeFormation(Formation formation, String teamId,String matchId){
        log.info("Starting formation change. teamId={}, matchId={}, formation={}",
                teamId, matchId, formation);

        validateSchedule(matchId, teamId);

        TeamEntity team  = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        team.setFormation(formation);
        teamRepository.save(team);

        log.info("Formation changed successfully. teamId={}, matchId={}, formation={}",
                teamId, matchId, formation);

    }

    private void validateSchedule(String matchId, String teamId) {
        MatchEntity match = matchRepository.findByIdAndTeam(matchId, teamId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        if (DateUtil.isInThePast(match.getDateTime())) {
            throw new ScheduleConflictException(
                    "Cannot change formation, match has already started"
            );
        }

        if (DateUtil.isWithinOneHour(match.getDateTime())) {
            throw new ScheduleConflictException(
                    String.format("Cannot change formation, match starts in %d minutes",
                            DateUtil.minutesUntil(match.getDateTime()))
            );
        }
    }
}