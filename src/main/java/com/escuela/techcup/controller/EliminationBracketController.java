package com.escuela.techcup.controller;

import com.escuela.techcup.core.model.EliminationBracket;
import com.escuela.techcup.core.service.EliminationBracketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brackets")
public class EliminationBracketController {

    @Autowired
    private EliminationBracketService bracketService;

    @GetMapping("/{tournamentId}")
    public Response getBrackets(@PathVariable String tournamentId) {
        EliminationBracket bracket = bracketService.getBracketsForTournament(tournamentId);
        if (bracket == null) {
            return new Response(null, "No se pueden generar llaves: hay partidos pendientes o no hay suficientes equipos.");
        }
        return new Response(bracket, "Llaves generadas correctamente");
    }

    public static class Response {
        private EliminationBracket eliminationBrackets;
        private String message;

        public Response(EliminationBracket eliminationBrackets, String message) {
            this.eliminationBrackets = eliminationBrackets;
            this.message = message;
        }
        public EliminationBracket getEliminationBrackets() { return eliminationBrackets; }
        public String getMessage() { return message; }
    }
}
