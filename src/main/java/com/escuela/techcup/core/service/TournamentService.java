package com.escuela.techcup.core.service;

import com.escuela.techcup.core.model.Tournament;
import com.escuela.techcup.core.model.enums.TournamentStatus;

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

    // RF-07: Configurar reglamento, fechas, canchas
    Tournament configureTournament(String tournamentId, String reglamento,
                                   LocalDateTime closingDate, String canchas,
                                   String horarios, String sanciones);

    // Obtener torneo activo (usado por TeamService)
    Tournament getActiveTournament();
}