package com.escuela.techcup.controller;

import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.MatchEvent;
import com.escuela.techcup.core.service.StandingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Tabla de posiciones y estadísticas", description = "Consulta y gestión automática de la tabla de posiciones, ranking de goleadores y tarjetas del torneo. Permite visualizar estadísticas actualizadas tras cada partido, incluyendo partidos jugados, ganados, empatados, perdidos, goles a favor/en contra, diferencia de gol, puntos, ranking de goleadores y tarjetas amarillas/rojas por jugador y equipo.")
@RestController
@RequestMapping("/api/standings")
public class StandingsController {

    @Autowired
    private StandingsService standingsService;


    @GetMapping("/{tournamentId}")
    public List<Team> getStandingsTable(@PathVariable String tournamentId) {
        return standingsService.getStandingsTable(tournamentId);
    }


    @GetMapping("/{tournamentId}/top-scorers")
    public List<Player> getTopScorers(@PathVariable String tournamentId) {
        return standingsService.getTopScorers(tournamentId);
    }


    @GetMapping("/{tournamentId}/cards-history")
    public List<MatchEvent> getCardsHistory(@PathVariable String tournamentId,
                                            @RequestParam(required = false) String playerOrTeamId) {
        return standingsService.getCardsHistory(tournamentId, playerOrTeamId);
    }
}
