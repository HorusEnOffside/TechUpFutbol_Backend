
package com.escuela.techcup.core.service.impl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.escuela.techcup.controller.dto.MatchResultDTO;
import com.escuela.techcup.controller.dto.PlayerMatchStatsDTO;
import com.escuela.techcup.core.exception.MatchNotFoundException;
import com.escuela.techcup.core.exception.ScheduleConflictException;
import com.escuela.techcup.core.exception.TeamNotFoundException;
import com.escuela.techcup.core.exception.UserNotFoundException;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.Goal;
import com.escuela.techcup.core.model.Match;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.service.MatchService;
import com.escuela.techcup.core.service.PlayerService;
import com.escuela.techcup.core.service.SoccerFieldService;
import com.escuela.techcup.core.util.IdGeneratorUtil;
import com.escuela.techcup.persistence.entity.tournament.CardEntity;
import com.escuela.techcup.persistence.entity.tournament.GoalEntity;
import com.escuela.techcup.persistence.entity.tournament.MatchEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.RefereeEntity;
import com.escuela.techcup.persistence.mapper.tournament.GoalMapper;
import com.escuela.techcup.persistence.mapper.tournament.MatchMapper;
import com.escuela.techcup.persistence.mapper.tournament.SoccerFieldMapper;
import com.escuela.techcup.persistence.mapper.tournament.TeamMapper;
import com.escuela.techcup.persistence.mapper.users.PlayerMapper;
import com.escuela.techcup.persistence.repository.tournament.MatchRepository;
import com.escuela.techcup.persistence.repository.tournament.TeamRepository;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;
import com.escuela.techcup.persistence.repository.users.RefereeRepository;

@Service
public class MatchServiceImpl implements MatchService {

    private static final Logger log = LoggerFactory.getLogger(MatchServiceImpl.class);

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final RefereeRepository refereeRepository;
    private final SoccerFieldService SoccerFieldServiceImpl;
    private final PlayerService playerService;
    private final PlayerRepository playerRepository;

    public MatchServiceImpl(MatchRepository matchRepository, TeamRepository teamRepository, RefereeRepository refereeRepository, SoccerFieldService SoccerFieldServiceImpl, PlayerService playerService, PlayerRepository playerRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.refereeRepository = refereeRepository;
        this.SoccerFieldServiceImpl = SoccerFieldServiceImpl;
        this.playerService = playerService;
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Match getMatchById(String matchId) {
        return matchRepository.findById(UUID.fromString(matchId))
                .map(MatchMapper::toModel)
                .orElseThrow(() -> new MatchNotFoundException(matchId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Match> getAllMatches() {
        return matchRepository.findAll().stream()
                .map(MatchMapper::toModel)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Match> getMatchesByRefereeId(String refereeId) {
        return matchRepository.findByRefereeId(UUID.fromString(refereeId)).stream()
                .map(MatchMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public Match createMatch(LocalDate date, String teamAId, String teamBId) {
        log.info("Creating match for date: {}, teamAId: {}, teamBId: {}", date, teamAId, teamBId);

        if (teamAId.equals(teamBId)) {
            throw new InvalidInputException("Un equipo no puede jugar contra sí mismo");
        }

        TeamEntity teamA = teamRepository.findById(UUID.fromString(teamAId))
                .orElseThrow(() -> new TeamNotFoundException(teamAId));
        TeamEntity teamB = teamRepository.findById(UUID.fromString(teamBId))
                .orElseThrow(() -> new TeamNotFoundException(teamBId));

        LocalDateTime dateTime = date.atStartOfDay();

        // RN: un equipo no puede jugar dos partidos a la misma hora
        if (matchRepository.existsByDateTimeAndTeamAIdOrDateTimeAndTeamBId(dateTime, UUID.fromString(teamAId), dateTime, UUID.fromString(teamAId))
                || matchRepository.existsByDateTimeAndTeamAIdOrDateTimeAndTeamBId(dateTime, UUID.fromString(teamBId), dateTime, UUID.fromString(teamBId))) {
            throw new ScheduleConflictException("One of the teams already has a match scheduled at that date and time");
        }

        Match match = new Match(idGenerator(), dateTime, TeamMapper.toModel(teamA), TeamMapper.toModel(teamB));
        MatchEntity matchEntity = MatchMapper.toEntity(match);
        matchRepository.save(matchEntity);
        return match;
    }

    @Override
    @Transactional
    public Match setReferee(String matchId, String refereeId) {
        MatchEntity matchEntity = matchRepository.findById(UUID.fromString(matchId))
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        RefereeEntity refereeEntity = refereeRepository.findById(UUID.fromString(refereeId))
                .orElseThrow(() -> new UserNotFoundException(refereeId));

        matchEntity.setReferee(refereeEntity);
        matchRepository.save(matchEntity);
        return MatchMapper.toModel(matchEntity);
    }


    @Override
    @Transactional
    public Match setSoccerField(String matchId, String soccerFieldId) {
        MatchEntity matchEntity = matchRepository.findById(UUID.fromString(matchId))
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        // RN: una cancha no puede tener dos partidos a la misma hora
        if (matchRepository.existsByDateTimeAndSoccerFieldId(matchEntity.getDateTime(), UUID.fromString(soccerFieldId))) {
            throw new ScheduleConflictException("The soccer field already has a match scheduled at that date and time");
        }

        matchEntity.setSoccerField(SoccerFieldMapper.toEntity(SoccerFieldServiceImpl.getSoccerFieldById(soccerFieldId)));
        matchRepository.save(matchEntity);
        return MatchMapper.toModel(matchEntity);

    }

    @Override
    @Transactional
    public Match addMatchEventGoal(String matchId, String playerId, int minute, String description) {
        log.info("Adding goal to match {}. playerId={}, minute={}", matchId, playerId, minute);
        MatchEntity matchEntity = matchRepository.findById(UUID.fromString(matchId))
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        PlayerEntity playerEntity = playerRepository.findByUserId(UUID.fromString(playerId))
                .orElseThrow(() -> new UserNotFoundException(playerId));

        validatePlayerBelongsToMatch(playerEntity, matchEntity);

        Player player = PlayerMapper.toModel(playerEntity);
        Goal goal = new Goal(idGenerator(), minute, player, description);
        matchEntity.addGoal(GoalMapper.toEntity(goal));
        matchRepository.save(matchEntity);
        return MatchMapper.toModel(matchEntity);
    }

    @Override
    @Transactional
    public Match addMatchEventCard(String matchId, String playerId, int minute, CardEntity.CardType type, String description) {
        log.info("Adding card to match {}. playerId={}, minute={}, type={}", matchId, playerId, minute, type);
        MatchEntity matchEntity = matchRepository.findById(UUID.fromString(matchId))
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        PlayerEntity playerEntity = playerRepository.findByUserId(UUID.fromString(playerId))
                .orElseThrow(() -> new UserNotFoundException(playerId));

        validatePlayerBelongsToMatch(playerEntity, matchEntity);

        CardEntity card = new CardEntity();
        card.setMinute(minute);
        card.setType(type);
        card.setDescription(description);
        card.setPlayer(playerEntity);
        matchEntity.addCard(card);

        if (type == CardEntity.CardType.RED) {
            log.info("Red card issued to playerId={}. Setting status to NOT_AVAILABLE.", playerId);
            playerEntity.setStatus(PlayerStatus.NOT_AVAILABLE);
            playerRepository.save(playerEntity);
        }

        matchRepository.save(matchEntity);
        return MatchMapper.toModel(matchEntity);
    }

    @Override
    @Transactional
    public Match finalizeMatch(String matchId, MatchResultDTO result) {
        log.info("Finalizing match {}. localScore={}, visitorScore={}", matchId, result.getLocalScore(), result.getVisitorScore());
        MatchEntity matchEntity = matchRepository.findById(UUID.fromString(matchId))
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        matchEntity.setLocalScore(result.getLocalScore());
        matchEntity.setVisitorScore(result.getVisitorScore());

        if (result.getPlayerStats() != null) {
            for (PlayerMatchStatsDTO stats : result.getPlayerStats()) {
                PlayerEntity playerEntity = playerRepository.findByUserId(UUID.fromString(stats.getPlayerId()))
                        .orElse(null);
                if (playerEntity == null) continue;

                for (int i = 0; i < stats.getGoals(); i++) {
                    GoalEntity goal = new GoalEntity();
                    goal.setId(UUID.randomUUID());
                    goal.setMinute(0);
                    goal.setDescription("Gol registrado al finalizar partido");
                    goal.setPlayer(playerEntity);
                    matchEntity.addGoal(goal);
                }

                for (int i = 0; i < stats.getYellowCards(); i++) {
                    CardEntity card = new CardEntity();
                    card.setMinute(0);
                    card.setType(CardEntity.CardType.YELLOW);
                    card.setDescription("Tarjeta amarilla registrada al finalizar partido");
                    card.setPlayer(playerEntity);
                    matchEntity.addCard(card);
                }

                for (int i = 0; i < stats.getRedCards(); i++) {
                    CardEntity card = new CardEntity();
                    card.setMinute(0);
                    card.setType(CardEntity.CardType.RED);
                    card.setDescription("Tarjeta roja registrada al finalizar partido");
                    card.setPlayer(playerEntity);
                    matchEntity.addCard(card);
                    log.info("Red card from match result for playerId={}. Setting status to NOT_AVAILABLE.", stats.getPlayerId());
                    playerEntity.setStatus(PlayerStatus.NOT_AVAILABLE);
                    playerRepository.save(playerEntity);
                }
            }
        }

        matchEntity.setStatus("FINISHED");
        matchRepository.save(matchEntity);
        return MatchMapper.toModel(matchEntity);
    }

    private void validatePlayerBelongsToMatch(PlayerEntity player, MatchEntity match) {
        if (player.getTeam() == null) {
            throw new InvalidInputException("El jugador no pertenece a ningún equipo");
        }
        UUID playerTeamId = player.getTeam().getId();
        boolean inTeamA = match.getTeamA() != null && match.getTeamA().getId().equals(playerTeamId);
        boolean inTeamB = match.getTeamB() != null && match.getTeamB().getId().equals(playerTeamId);
        if (!inTeamA && !inTeamB) {
            throw new InvalidInputException("El jugador no pertenece a ninguno de los equipos del partido");
        }
    }

    private String idGenerator() {
        return IdGeneratorUtil.generateId();
    }
}
