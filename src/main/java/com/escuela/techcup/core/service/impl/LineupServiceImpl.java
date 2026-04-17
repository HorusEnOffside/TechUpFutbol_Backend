package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.LineupPlayerDTO;
import com.escuela.techcup.controller.dto.LineupRequestDTO;
import com.escuela.techcup.controller.dto.LineupResponseDTO;
import com.escuela.techcup.controller.dto.LineupResponseDTO.LineupPlayerResponseDTO;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.exception.MatchNotFoundException;
import com.escuela.techcup.core.service.LineupService;
import com.escuela.techcup.core.util.IdGeneratorUtil;
import com.escuela.techcup.persistence.entity.tournament.LineupEntity;
import com.escuela.techcup.persistence.entity.tournament.LineupPlayerEntity;
import com.escuela.techcup.persistence.entity.tournament.MatchEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.repository.tournament.LineupRepository;
import com.escuela.techcup.persistence.repository.tournament.MatchRepository;
import com.escuela.techcup.persistence.repository.tournament.TeamRepository;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LineupServiceImpl implements LineupService {

    private static final Logger log = LoggerFactory.getLogger(LineupServiceImpl.class);

    private final LineupRepository lineupRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public LineupServiceImpl(LineupRepository lineupRepository, MatchRepository matchRepository,
                             TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.lineupRepository = lineupRepository;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional
    public LineupResponseDTO submitLineup(LineupRequestDTO request) {
        if (request.getPlayers() == null || request.getPlayers().isEmpty())
            throw new InvalidInputException("La alineación debe tener al menos un jugador");

        MatchEntity match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new MatchNotFoundException(request.getMatchId()));

        TeamEntity team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new InvalidInputException("Equipo no encontrado: " + request.getTeamId()));

        if (lineupRepository.existsByMatchIdAndTeamId(request.getMatchId(), request.getTeamId()))
            throw new InvalidInputException("Ya existe una alineación para este equipo en este partido");

        LineupEntity lineup = new LineupEntity();
        lineup.setId(IdGeneratorUtil.generateId());
        lineup.setMatch(match);
        lineup.setTeam(team);
        lineup.setFormation(request.getFormation());
        lineup.setStatus("SUBMITTED");

        for (LineupPlayerDTO p : request.getPlayers()) {
            PlayerEntity playerEntity = playerRepository.findByUserId(p.getPlayerId())
                    .orElseThrow(() -> new InvalidInputException("Jugador no encontrado: " + p.getPlayerId()));
            LineupPlayerEntity lp = new LineupPlayerEntity();
            lp.setId(IdGeneratorUtil.generateId());
            lp.setLineup(lineup);
            lp.setPlayer(playerEntity);
            lp.setPosition(p.getPosition());
            lp.setDorsalNumber(p.getDorsalNumber());
            lineup.getPlayers().add(lp);
        }

        lineupRepository.save(lineup);
        log.info("Lineup submitted. lineupId={}, matchId={}, teamId={}", lineup.getId(), request.getMatchId(), request.getTeamId());
        return toDTO(lineup);
    }

    @Override
    @Transactional(readOnly = true)
    public LineupResponseDTO getLineup(String matchId, String teamId) {
        LineupEntity lineup = lineupRepository.findByMatchIdAndTeamId(matchId, teamId)
                .orElseThrow(() -> new InvalidInputException("No se encontró alineación para matchId=" + matchId + " teamId=" + teamId));
        return toDTO(lineup);
    }

    @Override
    @Transactional
    public LineupResponseDTO validateLineup(String lineupId) {
        LineupEntity lineup = lineupRepository.findById(lineupId)
                .orElseThrow(() -> new InvalidInputException("Alineación no encontrada: " + lineupId));

        List<LineupPlayerEntity> players = lineup.getPlayers();
        if (players == null || players.size() < 7)
            throw new InvalidInputException("La alineación debe tener mínimo 7 jugadores");

        boolean hasGoalkeeper = players.stream()
                .anyMatch(p -> p.getPosition() != null &&
                        p.getPosition().name().equalsIgnoreCase("GOALKEEPER"));
        if (!hasGoalkeeper)
            throw new InvalidInputException("La alineación debe incluir un portero");

        lineup.setStatus("VALIDATED");
        lineupRepository.save(lineup);
        log.info("Lineup validated. lineupId={}", lineupId);
        return toDTO(lineup);
    }

    private LineupResponseDTO toDTO(LineupEntity lineup) {
        List<LineupPlayerResponseDTO> playerDTOs = lineup.getPlayers().stream()
                .map(p -> new LineupPlayerResponseDTO(
                        p.getPlayer().getId(),
                        p.getPlayer().getUser() != null ? p.getPlayer().getUser().getName() : null,
                        p.getPosition() != null ? p.getPosition().name() : null,
                        p.getDorsalNumber()
                ))
                .toList();

        return new LineupResponseDTO(
                lineup.getId(),
                lineup.getMatch().getId(),
                lineup.getTeam().getId(),
                lineup.getTeam().getName(),
                lineup.getFormation(),
                lineup.getStatus(),
                playerDTOs
        );
    }
}
