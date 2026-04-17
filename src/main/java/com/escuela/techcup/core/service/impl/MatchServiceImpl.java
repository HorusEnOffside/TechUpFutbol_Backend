
package com.escuela.techcup.core.service.impl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
import com.escuela.techcup.core.model.Card;
import com.escuela.techcup.core.model.Goal;
import com.escuela.techcup.core.model.Match;
import com.escuela.techcup.core.model.Player;
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
import com.escuela.techcup.persistence.mapper.tournament.CardMapper;
import com.escuela.techcup.persistence.mapper.tournament.GoalMapper;
import com.escuela.techcup.persistence.mapper.tournament.MatchMapper;
import com.escuela.techcup.persistence.mapper.tournament.SoccerFieldMapper;
import com.escuela.techcup.persistence.mapper.tournament.TeamMapper;
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
        return matchRepository.findById(matchId)
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
        return matchRepository.findByRefereeId(refereeId).stream()
                .map(MatchMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public Match createMatch(LocalDate date, String teamAId, String teamBId) {
        log.info("Creating match for date: {}, teamAId: {}, teamBId: {}", date, teamAId, teamBId);
        
        TeamEntity teamA = teamRepository.findById(teamAId)
                .orElseThrow(() -> new TeamNotFoundException(teamAId));
        TeamEntity teamB = teamRepository.findById(teamBId)
                .orElseThrow(() -> new TeamNotFoundException(teamBId));

        LocalDateTime dateTime = date.atStartOfDay();

        // RN: un equipo no puede jugar dos partidos a la misma hora
        if (matchRepository.existsByDateTimeAndTeamAIdOrDateTimeAndTeamBId(dateTime, teamAId, dateTime, teamAId)
                || matchRepository.existsByDateTimeAndTeamAIdOrDateTimeAndTeamBId(dateTime, teamBId, dateTime, teamBId)) {
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
        MatchEntity matchEntity = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        RefereeEntity refereeEntity = refereeRepository.findById(refereeId)
                .orElseThrow(() -> new UserNotFoundException(refereeId));

        matchEntity.setReferee(refereeEntity);
        matchRepository.save(matchEntity);
        return MatchMapper.toModel(matchEntity);
    }


    @Override
    @Transactional
    public Match setSoccerField(String matchId, String soccerFieldId) {
        MatchEntity matchEntity = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        // RN: una cancha no puede tener dos partidos a la misma hora
        if (matchRepository.existsByDateTimeAndSoccerFieldId(matchEntity.getDateTime(), soccerFieldId)) {
            throw new ScheduleConflictException("The soccer field already has a match scheduled at that date and time");
        }

        matchEntity.setSoccerField(SoccerFieldMapper.toEntity(SoccerFieldServiceImpl.getSoccerFieldById(soccerFieldId)));
        matchRepository.save(matchEntity);
        return MatchMapper.toModel(matchEntity);

    }

    @Override
    @Transactional
    public Match addMatchEventGoal(String matchId, String playerId, int minute, String description) {
        MatchEntity matchEntity = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        Player player = playerService.getPlayerByUserId(playerId)
                .orElseThrow(() -> new UserNotFoundException(playerId));
        
        Goal goal = new Goal(idGenerator(), minute, player, description);

        matchEntity.addGoal(GoalMapper.toEntity(goal));

        matchRepository.save(matchEntity);
        return MatchMapper.toModel(matchEntity);
    }

    @Override
    @Transactional
    public Match addMatchEventCard(String matchId, String playerId, int minute, CardEntity.CardType type, String description) {
        MatchEntity matchEntity = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        PlayerEntity playerEntity = playerRepository.findByUserId(playerId)
                .orElseThrow(() -> new UserNotFoundException(playerId));

        CardEntity card = new CardEntity();
        card.setMinute(minute);
        card.setType(type);
        card.setDescription(description);
        card.setPlayer(playerEntity);
        matchEntity.addCard(card);

        matchRepository.save(matchEntity);
        return MatchMapper.toModel(matchEntity);
    }

    @Override
    @Transactional
    public Match finalizeMatch(String matchId, MatchResultDTO result) {
        MatchEntity matchEntity = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        matchEntity.setLocalScore(result.getLocalScore());
        matchEntity.setVisitorScore(result.getVisitorScore());

        if (result.getPlayerStats() != null) {
            for (PlayerMatchStatsDTO stats : result.getPlayerStats()) {
                PlayerEntity playerEntity = playerRepository.findByUserId(stats.getPlayerId())
                        .orElse(null);
                if (playerEntity == null) continue;

                for (int i = 0; i < stats.getGoals(); i++) {
                    GoalEntity goal = new GoalEntity();
                    goal.setId(idGenerator());
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
                }
            }
        }

        matchEntity.setStatus("FINISHED");
        matchRepository.save(matchEntity);
        return MatchMapper.toModel(matchEntity);
    }

    private String idGenerator() {
        return IdGeneratorUtil.generateId();
    }
}
