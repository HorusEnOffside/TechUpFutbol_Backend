package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.PaymentDTO;
import com.escuela.techcup.core.exception.*;
import com.escuela.techcup.core.model.Invitation;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.Tournament;
import com.escuela.techcup.core.model.enums.Career;
import com.escuela.techcup.core.model.enums.Formation;
import com.escuela.techcup.core.model.enums.InvitationStatus;
import com.escuela.techcup.core.model.enums.TournamentStatus;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.core.service.PaymentService;
import com.escuela.techcup.core.service.TeamService;
import com.escuela.techcup.core.util.DateUtil;
import com.escuela.techcup.core.util.IdGeneratorUtil;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
import com.escuela.techcup.persistence.entity.tournament.InvitationEntity;
import com.escuela.techcup.persistence.entity.tournament.MatchEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamPlayerEntity;
import com.escuela.techcup.persistence.entity.tournament.TournamentEntity;
import com.escuela.techcup.persistence.entity.users.GraduateEntity;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.StudentEntity;
import com.escuela.techcup.persistence.entity.users.TeacherEntity;
import com.escuela.techcup.persistence.entity.users.UserEntity;
import com.escuela.techcup.persistence.mapper.TeamMapper;
import com.escuela.techcup.persistence.mapper.payment.PaymentMapper;
import com.escuela.techcup.persistence.repository.payment.PaymentRepository;
import com.escuela.techcup.persistence.repository.tournament.InvitationRepository;
import com.escuela.techcup.persistence.repository.tournament.MatchRepository;
import com.escuela.techcup.persistence.repository.tournament.TeamPlayerRepository;
import com.escuela.techcup.persistence.repository.tournament.TeamRepository;
import com.escuela.techcup.persistence.repository.tournament.TournamentRepository;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;
import com.escuela.techcup.persistence.repository.users.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class TeamServiceImpl implements TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;
    private final TeamPlayerRepository teamPlayerRepository;
    private final PlayerRepository playerRepository;
    private final InvitationRepository invitationRepository;
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public TeamServiceImpl(
            TeamRepository teamRepository,
            TeamPlayerRepository teamPlayerRepository,
            PlayerRepository playerRepository,
            InvitationRepository invitationRepository,
            TournamentRepository tournamentRepository,
            UserRepository userRepository,
            MatchRepository matchRepository,
            PaymentRepository paymentRepository,
            PaymentService paymentService) {
        this.teamRepository = teamRepository;
        this.teamPlayerRepository = teamPlayerRepository;
        this.playerRepository = playerRepository;
        this.invitationRepository = invitationRepository;
        this.tournamentRepository = tournamentRepository;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public Team createTeam(String name, String uniformColors, BufferedImage logo, String captainUserId) {
        if (name == null || name.isBlank()) throw new InvalidInputException("Team name is required");
        if (name.length() < 5) throw new InvalidInputException("Team name must have at least 5 characters");
        if (uniformColors == null || uniformColors.isBlank()) throw new InvalidInputException("Uniform colors are required");
        if (captainUserId == null || captainUserId.isBlank()) throw new InvalidInputException("captainUserId is required");
        if (teamRepository.existsByNameIgnoreCase(name)) throw new TeamAlreadyExistsException(name);

        TournamentEntity activeTournament = tournamentRepository
                .findByStatus(TournamentStatus.ACTIVE)
                .stream().findFirst()
                .orElseThrow(TournamentNotActiveException::new);

        PlayerEntity captainPlayer = playerRepository.findByUserId(UUID.fromString(captainUserId))
                .orElseThrow(() -> new InvalidInputException("El usuario no tiene perfil deportivo registrado. Completa tu perfil deportivo antes de crear un equipo."));

        UserEntity captainUser = captainPlayer.getUser();
        captainUser.addRole(UserRole.CAPTAIN);
        userRepository.save(captainUser);

        // El capitán pasa a estar en equipo
        captainPlayer.setStatus(com.escuela.techcup.core.model.enums.PlayerStatus.NOT_AVAILABLE);
        playerRepository.save(captainPlayer);

        String teamId = IdGeneratorUtil.generateId();
        Team team = new Team(teamId, name, uniformColors, logo, null);

        TeamEntity entity = TeamMapper.toEntity(team);
        entity.setTournament(activeTournament);
        entity.setCaptainPlayer(captainPlayer);
        teamRepository.save(entity);

        TeamPlayerEntity teamPlayer = new TeamPlayerEntity();
        teamPlayer.setId(UUID.randomUUID());
        teamPlayer.setTeam(entity);
        teamPlayer.setPlayer(captainPlayer);
        teamPlayerRepository.save(teamPlayer);

        log.info("Team created. teamId={}, captainUserId={}, tournamentId={}", teamId, captainUserId, activeTournament.getId());
        return team;
    }

    @Override
    @Transactional
    public void invitePlayer(String teamId, String playerId, String message) {
        if (teamId == null || teamId.isBlank()) throw new InvalidInputException("teamId is required");
        if (playerId == null || playerId.isBlank()) throw new InvalidInputException("playerId is required");

        TeamEntity team = teamRepository.findById(UUID.fromString(teamId))
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        PlayerEntity player = playerRepository.findById(UUID.fromString(playerId))
                .orElseThrow(() -> new InvalidInputException("Player not found"));

        if (invitationRepository.existsByTeamIdAndPlayerId(UUID.fromString(teamId), UUID.fromString(playerId)))
            throw new PlayerAlreadyInvitedException(playerId);

        List<TeamPlayerEntity> currentPlayers = teamPlayerRepository.findByTeamId(UUID.fromString(teamId));
        if (currentPlayers.size() >= 12)
            throw new InvalidInputException("Team already has the maximum of 12 players");

        if (team.getTournament() != null) {
            UUID tournamentId = team.getTournament().getId();
            if (teamPlayerRepository.existsByPlayerIdAndTournamentId(UUID.fromString(playerId), tournamentId))
                throw new InvalidInputException("Player is already registered in another team for this tournament");
        }

        if (!currentPlayers.isEmpty() && !isMajorityEngineering(currentPlayers))
            throw new InvalidInputException("Team must have a majority of Engineering or Data Science players");

        InvitationEntity invitation = new InvitationEntity();
        invitation.setId(UUID.randomUUID());
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
        if (invitationId == null || invitationId.isBlank()) throw new InvalidInputException("invitationId is required");
        if (action == null) throw new InvalidInputException("action is required");

        InvitationEntity invitation = invitationRepository
                .findByIdAndStatus(UUID.fromString(invitationId), InvitationStatus.PENDING)
                .orElseThrow(() -> new InvitationNotFoundException(invitationId));

        invitation.setStatus(action);
        invitationRepository.save(invitation);

        if (action == InvitationStatus.ACCEPTED) {
            UserEntity user = invitation.getPlayer().getUser();
            user.addRole(UserRole.PLAYER);
            userRepository.save(user);

            // El jugador pasa a estar en equipo
            PlayerEntity player = invitation.getPlayer();
            player.setStatus(com.escuela.techcup.core.model.enums.PlayerStatus.NOT_AVAILABLE);
            playerRepository.save(player);

            TeamPlayerEntity teamPlayer = new TeamPlayerEntity();
            teamPlayer.setId(UUID.randomUUID());
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
    public List<Invitation> getInvitationsByPlayer(String playerId) {
        if (playerId == null || playerId.isBlank()) throw new InvalidInputException("playerId is required");

        playerRepository.findById(UUID.fromString(playerId))
                .orElseThrow(() -> new InvalidInputException("Player not found"));

        return invitationRepository.findByPlayerId(UUID.fromString(playerId)).stream()
                .map(entity -> {
                    Invitation inv = new Invitation();
                    inv.setId(entity.getId() != null ? entity.getId().toString() : null);
                    inv.setTeamId(entity.getTeam().getId() != null ? entity.getTeam().getId().toString() : null);
                    inv.setTeamName(entity.getTeam().getName());
                    inv.setPlayerId(entity.getPlayer().getId() != null ? entity.getPlayer().getId().toString() : null);
                    inv.setMessage(entity.getMessage());
                    inv.setStatus(entity.getStatus());
                    return inv;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateTeamComposition(String teamId) {
        if (teamId == null || teamId.isBlank()) throw new InvalidInputException("teamId is required");
        List<TeamPlayerEntity> players = teamPlayerRepository.findByTeamId(UUID.fromString(teamId));
        int count = players.size();
        boolean valid = count >= 7 && count <= 12;
        log.info("Team composition validation. teamId={}, players={}, valid={}", teamId, count, valid);
        return valid;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validatePlayerUniquePerTournament(String playerId, String tournamentId) {
        if (playerId == null || playerId.isBlank()) throw new InvalidInputException("playerId is required");
        if (tournamentId == null || tournamentId.isBlank()) throw new InvalidInputException("tournamentId is required");
        boolean exists = teamPlayerRepository.existsByPlayerIdAndTournamentId(UUID.fromString(playerId), UUID.fromString(tournamentId));
        log.info("Player unique validation. playerId={}, tournamentId={}, exists={}", playerId, tournamentId, exists);
        return !exists;
    }

    @Override
    @Transactional(readOnly = true)
    public Team getTeamById(String teamId) {
        if (teamId == null || teamId.isBlank()) throw new InvalidInputException("teamId is required");
        return teamRepository.findById(UUID.fromString(teamId))
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
        if (teamId == null || teamId.isBlank()) throw new InvalidInputException("teamId is required");
        List<TeamPlayerEntity> teamPlayers = teamPlayerRepository.findByTeamId(UUID.fromString(teamId));
        if (teamPlayers.isEmpty()) throw new InvalidInputException("Team has no players");

        long engineeringCount = teamPlayers.stream()
                .map(TeamPlayerEntity::getPlayer)
                .map(PlayerEntity::getUser)
                .filter(user -> {
                    if (user instanceof StudentEntity s) return isEngineeringCareer(s.getCareer());
                    else if (user instanceof TeacherEntity t) return isEngineeringCareer(t.getCareer());
                    else if (user instanceof GraduateEntity g) return isEngineeringCareer(g.getCareer());
                    return false;
                })
                .count();

        int total = teamPlayers.size();
        boolean valid = engineeringCount > total / 2.0;
        log.info("Engineering majority validation. teamId={}, total={}, engineering={}, valid={}",
                teamId, total, engineeringCount, valid);
        return valid;
    }

    @Override
    @Transactional
    public void changeFormation(Formation formation, String teamId, String matchId) {
        log.info("Starting formation change. teamId={}, matchId={}, formation={}", teamId, matchId, formation);

        validateSchedule(matchId, teamId);

        TeamEntity team = teamRepository.findById(UUID.fromString(teamId))
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        team.setFormation(formation);
        teamRepository.save(team);

        log.info("Formation changed successfully. teamId={}, matchId={}, formation={}", teamId, matchId, formation);
    }

    private void validateSchedule(String matchId, String teamId) {
        MatchEntity match = matchRepository.findByIdAndTeam(UUID.fromString(matchId), UUID.fromString(teamId))
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        if (DateUtil.isInThePast(match.getDateTime())) {
            throw new ScheduleConflictException("Cannot change formation, match has already started");
        }

        if (DateUtil.isWithinOneHour(match.getDateTime())) {
            throw new ScheduleConflictException(
                    String.format("Cannot change formation, match starts in %d minutes",
                            DateUtil.minutesUntil(match.getDateTime())));
        }
    }

    @Override
    public List<Formation> getAllFormations() {
        log.info("Fetching all formations from enum");
        return Arrays.stream(Formation.values()).toList();
    }

    @Override
    public Formation getEnemyFormation(String teamId) {
        TeamEntity team = teamRepository.findById(UUID.fromString(teamId))
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        return team.getFormation();
    }


    @Override
    @Transactional(readOnly = true)
    public java.util.Optional<Team> findByNameContaining(String name) {
        if (name == null || name.isBlank()) throw new InvalidInputException("name is required");
        return teamRepository.findByNameContainingIgnoreCase(name).stream()
                .findFirst()
                .map(TeamMapper::toModel);
    }

    @Override
    public Payment uploadPayment(String teamId, PaymentDTO paymentDTO, MultipartFile voucher) {

        TeamEntity team = teamRepository.findById(UUID.fromString(teamId))
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado: " + teamId));

        TournamentEntity tournament = team.getTournament();

        if (!DateUtil.isBeforeDeadline(paymentDTO.getPaymentDate(), tournament.getStartDate())) {
            throw new PaymentDateException(paymentDTO.getPaymentDate(), tournament.getStartDate());
        }

        return paymentService.createPayment(paymentDTO, voucher);

    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private boolean isEngineeringCareer(Career career) {
        return career == Career.INGENIERIA_DE_SISTEMAS
                || career == Career.INTELIGENCIA_ARTIFICIAL
                || career == Career.CIBERSEGURIDAD
                || career == Career.ESTADISTICA;
    }

    private boolean isMajorityEngineering(List<TeamPlayerEntity> teamPlayers) {
        long engineeringCount = teamPlayers.stream()
                .map(TeamPlayerEntity::getPlayer)
                .map(PlayerEntity::getUser)
                .filter(user -> {
                    if (user instanceof StudentEntity s) return isEngineeringCareer(s.getCareer());
                    else if (user instanceof TeacherEntity t) return isEngineeringCareer(t.getCareer());
                    else if (user instanceof GraduateEntity g) return isEngineeringCareer(g.getCareer());
                    return false;
                })
                .count();
        return engineeringCount > teamPlayers.size() / 2.0;
    }
}