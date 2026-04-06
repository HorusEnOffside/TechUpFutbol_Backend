package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.exception.TournamentFinalizedException;
import com.escuela.techcup.core.exception.TournamentNotActiveException;
import com.escuela.techcup.core.exception.TournamentNotFoundException;
import com.escuela.techcup.core.exception.TournamentOverlapException;
import com.escuela.techcup.core.model.Tournament;
import com.escuela.techcup.core.model.enums.TournamentStatus;
import com.escuela.techcup.core.service.TournamentService;
import com.escuela.techcup.core.util.IdGeneratorUtil;
import com.escuela.techcup.persistence.entity.tournament.TournamentEntity;
import com.escuela.techcup.persistence.entity.users.OrganizerEntity;
import com.escuela.techcup.persistence.mapper.tournament.TournamentMapper;
import com.escuela.techcup.persistence.repository.tournament.TournamentRepository;
import com.escuela.techcup.persistence.repository.users.OrganizerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TournamentServiceImpl implements TournamentService {

    private static final Logger log = LoggerFactory.getLogger(TournamentServiceImpl.class);

    private final TournamentRepository tournamentRepository;
    private final OrganizerRepository organizerRepository;

    public TournamentServiceImpl(TournamentRepository tournamentRepository,
                                 OrganizerRepository organizerRepository) {
        this.tournamentRepository = tournamentRepository;
        this.organizerRepository = organizerRepository;
    }

    // RF-05: Crear torneo
    @Override
    @Transactional
    public Tournament createTournament(LocalDateTime startDate, LocalDateTime endDate,
                                       int teamsMaxAmount, Double teamCost,
                                       TournamentStatus status, String organizerId) {
        if (startDate == null) throw new InvalidInputException("startDate is required");
        if (endDate == null) throw new InvalidInputException("endDate is required");
        if (endDate.isBefore(startDate)) throw new InvalidInputException("endDate must be after startDate");
        if (teamsMaxAmount < 2) throw new InvalidInputException("teamsMaxAmount must be at least 2");
        if (teamCost == null || teamCost < 0) throw new InvalidInputException("teamCost is required and must be positive");
        if (status == null) throw new InvalidInputException("status is required");

        // RN-05: No fechas sobrepuestas
        boolean overlap = tournamentRepository.findAll().stream()
                .anyMatch(t -> startDate.isBefore(t.getEndDate()) && endDate.isAfter(t.getStartDate()));
        if (overlap) throw new TournamentOverlapException();

        OrganizerEntity organizer = organizerRepository.findById(organizerId)
                .orElseThrow(() -> new InvalidInputException("Organizer not found"));

        TournamentEntity entity = new TournamentEntity();
        entity.setId(IdGeneratorUtil.generateId());
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        entity.setTeamsMaxAmount(teamsMaxAmount);
        entity.setTeamCost(teamCost);
        entity.setStatus(status);
        entity.setOrganizer(organizer);

        tournamentRepository.save(entity);
        log.info("Tournament created. id={}", entity.getId());
        return TournamentMapper.toModel(entity);
    }

    // RF-06: Consultar torneo
    @Override
    @Transactional(readOnly = true)
    public Tournament getTournamentById(String tournamentId) {
        if (tournamentId == null || tournamentId.isBlank())
            throw new InvalidInputException("tournamentId is required");
        return tournamentRepository.findById(tournamentId)
                .map(TournamentMapper::toModel)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll().stream()
                .map(TournamentMapper::toModel)
                .toList();
    }

    // RF-06: Modificar torneo
    @Override
    @Transactional
    public Tournament updateTournament(String tournamentId, LocalDateTime startDate,
                                       LocalDateTime endDate, int teamsMaxAmount,
                                       Double teamCost, TournamentStatus status) {
        if (tournamentId == null || tournamentId.isBlank())
            throw new InvalidInputException("tournamentId is required");

        TournamentEntity entity = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        // RN-06: No modificar torneos finalizados
        if (entity.getStatus() == TournamentStatus.COMPLETED)
            throw new TournamentFinalizedException(tournamentId);

        if (startDate != null) entity.setStartDate(startDate);
        if (endDate != null) entity.setEndDate(endDate);
        if (teamsMaxAmount >= 2) entity.setTeamsMaxAmount(teamsMaxAmount);
        if (teamCost != null) entity.setTeamCost(teamCost);
        if (status != null) entity.setStatus(status);

        tournamentRepository.save(entity);
        log.info("Tournament updated. id={}", tournamentId);
        return TournamentMapper.toModel(entity);
    }

    // RF-06: Finalizar torneo
    @Override
    @Transactional
    public void finalizeTournament(String tournamentId) {
        if (tournamentId == null || tournamentId.isBlank())
            throw new InvalidInputException("tournamentId is required");

        TournamentEntity entity = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        if (entity.getStatus() == TournamentStatus.COMPLETED)
            throw new TournamentFinalizedException(tournamentId);

        entity.setStatus(TournamentStatus.COMPLETED);
        tournamentRepository.save(entity);
        log.info("Tournament finalized. id={}", tournamentId);
    }

    // RF-07: Configurar torneo
    @Override
    @Transactional
    public Tournament configureTournament(String tournamentId, String reglamento,
                                          LocalDateTime closingDate, String canchas,
                                          String horarios, String sanciones) {
        if (tournamentId == null || tournamentId.isBlank())
            throw new InvalidInputException("tournamentId is required");
        if (reglamento == null || reglamento.isBlank())
            throw new InvalidInputException("reglamento is required");
        if (reglamento.length() > 2000)
            throw new InvalidInputException("reglamento must not exceed 2000 characters");
        if (closingDate == null)
            throw new InvalidInputException("closingDate is required");
        if (canchas == null || canchas.isBlank())
            throw new InvalidInputException("canchas is required");
        if (horarios == null || horarios.isBlank())
            throw new InvalidInputException("horarios is required");

        TournamentEntity entity = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        if (entity.getStatus() == TournamentStatus.COMPLETED)
            throw new TournamentFinalizedException(tournamentId);

        // RN-07: closingDate debe ser antes de startDate
        if (!closingDate.isBefore(entity.getStartDate()))
            throw new InvalidInputException("closingDate must be before startDate");

        entity.setReglamento(reglamento);
        entity.setClosingDate(closingDate);
        entity.setCanchas(canchas);
        entity.setHorarios(horarios);
        entity.setSanciones(sanciones);

        tournamentRepository.save(entity);
        log.info("Tournament configured. id={}", tournamentId);
        return TournamentMapper.toModel(entity);
    }

    // Usado por TeamService al crear equipo
    @Override
    @Transactional(readOnly = true)
    public Tournament getActiveTournament() {
        return tournamentRepository.findByStatus(TournamentStatus.ACTIVE)
                .stream().findFirst()
                .map(TournamentMapper::toModel)
                .orElseThrow(TournamentNotActiveException::new);
    }
}