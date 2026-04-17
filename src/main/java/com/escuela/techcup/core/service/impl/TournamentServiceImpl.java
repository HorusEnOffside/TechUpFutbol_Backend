package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.exception.TournamentFinalizedException;
import com.escuela.techcup.core.exception.TournamentNotActiveException;
import com.escuela.techcup.core.exception.TournamentNotFoundException;
import com.escuela.techcup.core.exception.TournamentOverlapException;
import com.escuela.techcup.core.model.Tournament;
import com.escuela.techcup.core.model.enums.CanchaTipo;
import com.escuela.techcup.core.model.enums.TournamentStatus;
import com.escuela.techcup.core.service.TournamentService;
import com.escuela.techcup.core.util.IdGeneratorUtil;
import com.escuela.techcup.persistence.entity.tournament.CanchaEntity;
import com.escuela.techcup.persistence.entity.tournament.HorarioEntity;
import com.escuela.techcup.persistence.entity.tournament.TournamentEntity;
import com.escuela.techcup.persistence.entity.users.OrganizerEntity;
import com.escuela.techcup.persistence.mapper.tournament.TournamentMapper;
import com.escuela.techcup.persistence.repository.tournament.TournamentRepository;
import com.escuela.techcup.persistence.repository.users.OrganizerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        // RN-05: endDate debe ser posterior a startDate
        if (endDate.isBefore(startDate)) throw new InvalidInputException("endDate must be after startDate");

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

    // RF-07a: Configurar reglamento, fecha de cierre y sanciones
    @Override
    @Transactional
    public Tournament configureTournament(String tournamentId, String reglamento,
                                          LocalDateTime closingDate, String sanciones) {
        TournamentEntity entity = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        if (entity.getStatus() == TournamentStatus.COMPLETED)
            throw new TournamentFinalizedException(tournamentId);

        // RN-07: closingDate debe ser antes de startDate
        if (!closingDate.isBefore(entity.getStartDate()))
            throw new InvalidInputException("closingDate must be before startDate");

        entity.setReglamento(reglamento);
        entity.setClosingDate(closingDate);
        entity.setSanciones(sanciones);

        tournamentRepository.save(entity);
        log.info("Tournament configured. id={}", tournamentId);
        return TournamentMapper.toModel(entity);
    }

    // RF-07b: Añadir cancha una por una
    @Override
    @Transactional
    public Tournament addCancha(String tournamentId, CanchaTipo tipo, String nombre) {
        TournamentEntity entity = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        if (entity.getStatus() == TournamentStatus.COMPLETED)
            throw new TournamentFinalizedException(tournamentId);

        CanchaEntity c = new CanchaEntity();
        c.setId(IdGeneratorUtil.generateId());
        c.setTipo(tipo.name());
        c.setNombre(nombre != null && !nombre.isBlank() ? nombre : tipo.getDisplayName());
        c.setFotoUrl(tipo.getFotoUrl());
        c.setTournament(entity);
        entity.getCanchas().add(c);

        tournamentRepository.save(entity);
        log.info("Cancha added to tournament. id={}, tipo={}", tournamentId, tipo);
        return TournamentMapper.toModel(entity);
    }

    // RF-07c: Añadir horario/jornada uno por uno
    @Override
    @Transactional
    public Tournament addHorario(String tournamentId, LocalDate fecha, String descripcion) {
        TournamentEntity entity = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        if (entity.getStatus() == TournamentStatus.COMPLETED)
            throw new TournamentFinalizedException(tournamentId);

        HorarioEntity h = new HorarioEntity();
        h.setId(IdGeneratorUtil.generateId());
        h.setFecha(fecha);
        h.setDescripcion(descripcion);
        h.setTournament(entity);
        entity.getHorarios().add(h);

        tournamentRepository.save(entity);
        log.info("Horario added to tournament. id={}, fecha={}", tournamentId, fecha);
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