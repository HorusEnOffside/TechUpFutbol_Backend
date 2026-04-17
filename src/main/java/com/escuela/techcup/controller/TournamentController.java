package com.escuela.techcup.controller;

import com.escuela.techcup.controller.dto.AddCanchaDTO;
import com.escuela.techcup.controller.dto.ConfigureTournamentDTO;
import com.escuela.techcup.controller.dto.CreateTournamentDTO;
import com.escuela.techcup.controller.dto.HorarioDTO;
import com.escuela.techcup.controller.dto.UpdateTournamentDTO;
import com.escuela.techcup.core.model.Tournament;
import com.escuela.techcup.core.service.TournamentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@Tag(name = "Gestion de torneos", description = "Operaciones de torneos")
public class TournamentController {

	private final TournamentService tournamentService;

	public TournamentController(TournamentService tournamentService) {
		this.tournamentService = tournamentService;
	}

	// RF-05: Crear torneo
	@PreAuthorize("hasRole('ORGANIZER')")
	@PostMapping
	public ResponseEntity<Tournament> createTournament(@Valid @RequestBody CreateTournamentDTO dto) {
		Tournament tournament = tournamentService.createTournament(
				dto.getStartDate(), dto.getEndDate(), dto.getTeamsMaxAmount(),
				dto.getTeamCost(), dto.getStatus(), dto.getOrganizerId());
		return ResponseEntity.status(HttpStatus.CREATED).body(tournament);
	}

	// RF-06: Consultar todos los torneos
	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<List<Tournament>> getAllTournaments() {
		return ResponseEntity.ok(tournamentService.getAllTournaments());
	}

	// RF-06: Consultar torneo por id
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{tournamentId}")
	public ResponseEntity<Tournament> getTournamentById(@PathVariable String tournamentId) {
		return ResponseEntity.ok(tournamentService.getTournamentById(tournamentId));
	}

	// RF-06: Modificar torneo
	@PreAuthorize("hasRole('ORGANIZER')")
	@PutMapping("/{tournamentId}")
	public ResponseEntity<Tournament> updateTournament(
			@PathVariable String tournamentId,
			@RequestBody UpdateTournamentDTO dto) {
		return ResponseEntity.ok(tournamentService.updateTournament(
				tournamentId, dto.getStartDate(), dto.getEndDate(),
				dto.getTeamsMaxAmount() != null ? dto.getTeamsMaxAmount() : 0,
				dto.getTeamCost(), dto.getStatus()));
	}

	// RF-06: Finalizar torneo
	@PreAuthorize("hasRole('ORGANIZER')")
	@PutMapping("/{tournamentId}/finalize")
	public ResponseEntity<Void> finalizeTournament(@PathVariable String tournamentId) {
		tournamentService.finalizeTournament(tournamentId);
		return ResponseEntity.ok().build();
	}

	// RF-07a: Configurar reglamento, fecha de cierre y sanciones
	@PreAuthorize("hasRole('ORGANIZER')")
	@PutMapping("/{tournamentId}/configure")
	public ResponseEntity<Tournament> configureTournament(
			@PathVariable String tournamentId,
			@Valid @RequestBody ConfigureTournamentDTO dto) {
		return ResponseEntity.ok(tournamentService.configureTournament(
				tournamentId, dto.getReglamento(), dto.getClosingDate(), dto.getSanciones()));
	}

	// RF-07b: Añadir cancha (imagen asignada automáticamente por tipo)
	@PreAuthorize("hasRole('ORGANIZER')")
	@PostMapping("/{tournamentId}/canchas")
	public ResponseEntity<Tournament> addCancha(
			@PathVariable String tournamentId,
			@Valid @RequestBody AddCanchaDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(
				tournamentService.addCancha(tournamentId, dto.getTipo(), dto.getNombre()));
	}

	// RF-07c: Añadir horario/jornada
	@PreAuthorize("hasRole('ORGANIZER')")
	@PostMapping("/{tournamentId}/horarios")
	public ResponseEntity<Tournament> addHorario(
			@PathVariable String tournamentId,
			@Valid @RequestBody HorarioDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(
				tournamentService.addHorario(tournamentId, dto.getFecha(), dto.getDescripcion()));
	}

	// Consultar torneo activo
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/active")
	public ResponseEntity<Tournament> getActiveTournament() {
		return ResponseEntity.ok(tournamentService.getActiveTournament());
	}

	@PreAuthorize("hasRole('ORGANIZER')")
	@GetMapping("/health")
	public String health() {
		return "Tournament controller OK";
	}
}