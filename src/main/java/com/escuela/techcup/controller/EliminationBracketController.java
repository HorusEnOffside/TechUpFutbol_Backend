package com.escuela.techcup.controller;

import com.escuela.techcup.core.model.EliminationBracket;
import com.escuela.techcup.core.service.EliminationBracketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Llaves de eliminación", description = "Generación y consulta de llaves de eliminación directa")
@RestController
@RequestMapping("/api/brackets")
public class EliminationBracketController {

    private static final Logger log = LoggerFactory.getLogger(EliminationBracketController.class);

    private final EliminationBracketService bracketService;

    public EliminationBracketController(EliminationBracketService bracketService) {
        this.bracketService = bracketService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{tournamentId}")
    public ResponseEntity<EliminationBracket> getBrackets(@PathVariable String tournamentId) {
        log.info("Request to get brackets for tournamentId={}", tournamentId);
        EliminationBracket bracket = bracketService.getBracketsForTournament(tournamentId);
        if (bracket == null) {
            log.warn("No brackets available for tournamentId={}", tournamentId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bracket);
    }
}
