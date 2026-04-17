package com.escuela.techcup.core.service;

import com.escuela.techcup.core.model.Tournament;
import com.escuela.techcup.core.model.enums.CanchaTipo;
import com.escuela.techcup.core.model.enums.TournamentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TournamentService {

    // RF-05: Crear torneo
    Tournament createTournament(LocalDateTime startDate, LocalDateTime endDate,
                                int teamsMaxAmount, Double teamCost,
                                TournamentStatus status, String organizerId);

    // RF-06: Consultar, modificar y finalizar torneo
    Tournament getTournamentById(String tournamentId);
    List<Tournament> getAllTournaments();
    Tournament updateTournament(String tournamentId, LocalDateTime startDate,
                                LocalDateTime endDate, int teamsMaxAmount,
                                Double teamCost, TournamentStatus status);
    void finalizeTournament(String tournamentId);

    // RF-07a: Configurar reglamento, fecha de cierre y sanciones
    Tournament configureTournament(String tournamentId, String reglamento,
                                   LocalDateTime closingDate, String sanciones);

    // RF-07b: Añadir cancha una por una (imagen asignada automáticamente por tipo)
    Tournament addCancha(String tournamentId, CanchaTipo tipo, String nombre);

    // RF-07c: Añadir horario/jornada uno por uno
    Tournament addHorario(String tournamentId, LocalDate fecha, String descripcion);

    // Obtener torneo activo (usado por TeamService)
    Tournament getActiveTournament();
}