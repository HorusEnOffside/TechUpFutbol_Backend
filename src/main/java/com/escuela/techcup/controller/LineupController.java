package com.escuela.techcup.controller;

import com.escuela.techcup.controller.dto.LineupRequestDTO;
import com.escuela.techcup.controller.dto.LineupResponseDTO;
import com.escuela.techcup.core.service.LineupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lineups")
@Tag(name = "Alineaciones", description = "Gestión de alineaciones por partido y equipo (RF-19, RF-20)")
public class LineupController {

    private static final Logger log = LoggerFactory.getLogger(LineupController.class);

    private final LineupService lineupService;

    public LineupController(LineupService lineupService) {
        this.lineupService = lineupService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CAPTAIN') or hasRole('ADMIN')")
    public ResponseEntity<LineupResponseDTO> submitLineup(@Valid @RequestBody LineupRequestDTO request) {
        log.info("Submit lineup. matchId={}, teamId={}", request.getMatchId(), request.getTeamId());
        return ResponseEntity.status(201).body(lineupService.submitLineup(request));
    }

    @GetMapping("/match/{matchId}/team/{teamId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LineupResponseDTO> getLineup(
            @PathVariable String matchId,
            @PathVariable String teamId) {
        log.info("Get lineup. matchId={}, teamId={}", matchId, teamId);
        return ResponseEntity.ok(lineupService.getLineup(matchId, teamId));
    }

    @PutMapping("/{lineupId}/validate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<LineupResponseDTO> validateLineup(@PathVariable String lineupId) {
        log.info("Validate lineup. lineupId={}", lineupId);
        return ResponseEntity.ok(lineupService.validateLineup(lineupId));
    }
}
