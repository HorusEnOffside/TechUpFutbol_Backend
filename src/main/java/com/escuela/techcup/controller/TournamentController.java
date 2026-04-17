package com.escuela.techcup.controller;

import com.escuela.techcup.controller.dto.AddCanchaDTO;
import com.escuela.techcup.controller.dto.ConfigureTournamentDTO;
import com.escuela.techcup.controller.dto.CreateTournamentDTO;
import com.escuela.techcup.controller.dto.HorarioDTO;
import com.escuela.techcup.controller.dto.TournamentResponseDTO;
import com.escuela.techcup.controller.dto.UpdateTournamentDTO;
import com.escuela.techcup.controller.mapper.TournamentResponseMapper;
import com.escuela.techcup.core.service.TournamentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@Tag(name = "Gestion de torneos", description = "Operaciones de torneos")
public class TournamentController {

	private static final Logger log = LoggerFactory.getLogger(TournamentController.class);

	private final TournamentService tournamentService;

	public TournamentController(TournamentService tournamentService) {
		this.tournamentService = tournamentService;
	}

	@PreAuthorize("hasRole('ORGANIZER')")
	@PostMapping
	public ResponseEntity<TournamentResponseDTO> createTournament(@Valid @RequestBody CreateTournamentDTO dto) {
		log.info("Request to create tournament. organizerId={}", dto.getOrganizerId());
		return ResponseEntity.status(HttpStatus.CREATED).body(
				TournamentResponseMapper.toDTO(tournamentService.createTournament(
						dto.getStartDate(), dto.getEndDate(), dto.getTeamsMaxAmount(),
						dto.getTeamCost(), dto.getStatus(), dto.getOrganizerId())));
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<List<TournamentResponseDTO>> getAllTournaments() {
		log.info("Request to get all tournaments");
		return ResponseEntity.ok(TournamentResponseMapper.toDTOList(tournamentService.getAllTournaments()));
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{tournamentId}")
	public ResponseEntity<TournamentResponseDTO> getTournamentById(@PathVariable String tournamentId) {
		log.info("Request to get tournament by id={}", tournamentId);
		return ResponseEntity.ok(TournamentResponseMapper.toDTO(tournamentService.getTournamentById(tournamentId)));
	}

	@PreAuthorize("hasRole('ORGANIZER')")
	@PutMapping("/{tournamentId}")
	public ResponseEntity<TournamentResponseDTO> updateTournament(
			@PathVariable String tournamentId,
			@Valid @RequestBody UpdateTournamentDTO dto) {
		log.info("Request to update tournament id={}", tournamentId);
		return ResponseEntity.ok(TournamentResponseMapper.toDTO(tournamentService.updateTournament(
				tournamentId, dto.getStartDate(), dto.getEndDate(),
				dto.getTeamsMaxAmount() != null ? dto.getTeamsMaxAmount() : 0,
				dto.getTeamCost(), dto.getStatus())));
	}

	@PreAuthorize("hasRole('ORGANIZER')")
	@PutMapping("/{tournamentId}/finalize")
	public ResponseEntity<Void> finalizeTournament(@PathVariable String tournamentId) {
		log.info("Request to finalize tournament id={}", tournamentId);
		tournamentService.finalizeTournament(tournamentId);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasRole('ORGANIZER')")
	@PutMapping("/{tournamentId}/configure")
	public ResponseEntity<TournamentResponseDTO> configureTournament(
			@PathVariable String tournamentId,
			@Valid @RequestBody ConfigureTournamentDTO dto) {
		log.info("Request to configure tournament id={}", tournamentId);
		return ResponseEntity.ok(TournamentResponseMapper.toDTO(tournamentService.configureTournament(
				tournamentId, dto.getReglamento(), dto.getClosingDate(), dto.getSanciones())));
	}

	@PreAuthorize("hasRole('ORGANIZER')")
	@PostMapping("/{tournamentId}/canchas")
	public ResponseEntity<TournamentResponseDTO> addCancha(
			@PathVariable String tournamentId,
			@Valid @RequestBody AddCanchaDTO dto) {
		log.info("Request to add cancha to tournament id={}", tournamentId);
		return ResponseEntity.status(HttpStatus.CREATED).body(
				TournamentResponseMapper.toDTO(tournamentService.addCancha(tournamentId, dto.getTipo(), dto.getNombre())));
	}

	@PreAuthorize("hasRole('ORGANIZER')")
	@PostMapping("/{tournamentId}/horarios")
	public ResponseEntity<TournamentResponseDTO> addHorario(
			@PathVariable String tournamentId,
			@Valid @RequestBody HorarioDTO dto) {
		log.info("Request to add horario to tournament id={}", tournamentId);
		return ResponseEntity.status(HttpStatus.CREATED).body(
				TournamentResponseMapper.toDTO(tournamentService.addHorario(tournamentId, dto.getFecha(), dto.getDescripcion())));
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/active")
	public ResponseEntity<TournamentResponseDTO> getActiveTournament() {
		log.info("Request to get active tournament");
		return ResponseEntity.ok(TournamentResponseMapper.toDTO(tournamentService.getActiveTournament()));
	}

	@PreAuthorize("hasRole('ORGANIZER')")
	@GetMapping("/health")
	public String health() {
		return "Tournament controller OK";
	}
}
