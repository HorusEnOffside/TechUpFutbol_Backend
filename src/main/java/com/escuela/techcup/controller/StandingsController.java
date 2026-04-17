package com.escuela.techcup.controller;

import com.escuela.techcup.core.model.MatchEvent;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.service.StandingsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tabla de posiciones y estadísticas", description = "Consulta y gestión automática de la tabla de posiciones, ranking de goleadores y tarjetas del torneo.")
@RestController
@RequestMapping("/api/standings")
public class StandingsController {

    private static final Logger log = LoggerFactory.getLogger(StandingsController.class);

    private final StandingsService standingsService;

    public StandingsController(StandingsService standingsService) {
        this.standingsService = standingsService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{tournamentId}")
    public ResponseEntity<List<Team>> getStandingsTable(@PathVariable String tournamentId) {
        log.info("Request to get standings table for tournamentId={}", tournamentId);
        return ResponseEntity.ok(standingsService.getStandingsTable(tournamentId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{tournamentId}/top-scorers")
    public ResponseEntity<List<Player>> getTopScorers(@PathVariable String tournamentId) {
        log.info("Request to get top scorers for tournamentId={}", tournamentId);
        return ResponseEntity.ok(standingsService.getTopScorers(tournamentId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{tournamentId}/cards-history")
    public ResponseEntity<List<MatchEvent>> getCardsHistory(
            @PathVariable String tournamentId,
            @RequestParam(required = false) String playerOrTeamId) {
        log.info("Request to get cards history for tournamentId={}, playerOrTeamId={}", tournamentId, playerOrTeamId);
        return ResponseEntity.ok(standingsService.getCardsHistory(tournamentId, playerOrTeamId));
    }
}
