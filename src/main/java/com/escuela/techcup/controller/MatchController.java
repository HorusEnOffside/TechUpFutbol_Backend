package com.escuela.techcup.controller;

import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.escuela.techcup.controller.dto.MatchResponseDTO;
import com.escuela.techcup.controller.dto.MatchResultDTO;
import com.escuela.techcup.controller.mapper.MatchResponseMapper;
import com.escuela.techcup.core.service.MatchService;
import com.escuela.techcup.persistence.entity.tournament.CardEntity;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/matches")
@Tag(name = "Gestion de partidos", description = "Operaciones de partidos")
public class MatchController {

	private static final Logger log = LoggerFactory.getLogger(MatchController.class);

	private final MatchService matchService;

	public MatchController(MatchService matchService) {
		this.matchService = matchService;
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('REFEREE') or hasRole('USER')")
	public ResponseEntity<MatchResponseDTO> getMatchById(@PathVariable String id) {
		log.info("Received request to get match by id: {}", id);
		return ResponseEntity.ok(MatchResponseMapper.toDTO(matchService.getMatchById(id)));
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN') or hasRole('REFEREE') or hasRole('USER')")
	public ResponseEntity<List<MatchResponseDTO>> getAllMatches() {
		log.info("Received request to get all matches");
		return ResponseEntity.ok(matchService.getAllMatches().stream()
				.map(MatchResponseMapper::toDTO).toList());
	}

	@GetMapping("/my-matches")
	@PreAuthorize("hasRole('REFEREE')")
	public ResponseEntity<List<MatchResponseDTO>> getMyMatches(Authentication auth) {
		String refereeId = auth.getName();
		log.info("Referee {} requested their matches", refereeId);
		return ResponseEntity.ok(matchService.getMatchesByRefereeId(refereeId).stream()
				.map(MatchResponseMapper::toDTO).toList());
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
	public ResponseEntity<MatchResponseDTO> createMatch(
			@RequestParam LocalDate date,
			@RequestParam String teamAId,
			@RequestParam String teamBId) {
		log.info("Received request to create match: date={}, teamAId={}, teamBId={}", date, teamAId, teamBId);
		return ResponseEntity.status(201).body(MatchResponseMapper.toDTO(matchService.createMatch(date, teamAId, teamBId)));
	}

	@PutMapping("/{matchId}/referee")
	@PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
	public ResponseEntity<MatchResponseDTO> setReferee(
			@PathVariable String matchId,
			@RequestParam String refereeId) {
		log.info("Received request to set referee for match. matchId={}, refereeId={}", matchId, refereeId);
		return ResponseEntity.ok(MatchResponseMapper.toDTO(matchService.setReferee(matchId, refereeId)));
	}

	@PostMapping("/{matchId}/soccer-field")
	@PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
	public ResponseEntity<MatchResponseDTO> setSoccerField(
			@PathVariable String matchId,
			@RequestParam String soccerFieldId) {
		log.info("Received request to set soccer field for match. matchId={}, soccerFieldId={}", matchId, soccerFieldId);
		return ResponseEntity.ok(MatchResponseMapper.toDTO(matchService.setSoccerField(matchId, soccerFieldId)));
	}

	@PostMapping("/{matchId}/events/goal")
	@PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER') or hasRole('REFEREE')")
	public ResponseEntity<MatchResponseDTO> addMatchEventGoal(
			@PathVariable String matchId,
			@RequestParam String playerId,
			@RequestParam int minute,
			@RequestParam String description) {
		log.info("Received request to add goal event to match. matchId={}, playerId={}, minute={}, description={}", matchId, playerId, minute, description);
		return ResponseEntity.ok(MatchResponseMapper.toDTO(matchService.addMatchEventGoal(matchId, playerId, minute, description)));
	}

	@PostMapping("/{matchId}/events/card")
	@PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER') or hasRole('REFEREE')")
	public ResponseEntity<MatchResponseDTO> addMatchEventCard(
			@PathVariable String matchId,
			@RequestParam String playerId,
			@RequestParam int minute,
			@RequestParam CardEntity.CardType type,
			@RequestParam(required = false) String description) {
		log.info("Received request to add card event to match. matchId={}, playerId={}, minute={}, type={}", matchId, playerId, minute, type);
		return ResponseEntity.ok(MatchResponseMapper.toDTO(matchService.addMatchEventCard(matchId, playerId, minute, type, description)));
	}

	@PostMapping("/{matchId}/result")
	@PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER') or hasRole('REFEREE')")
	public ResponseEntity<MatchResponseDTO> finalizeMatch(
			@PathVariable String matchId,
			@RequestBody MatchResultDTO result) {
		log.info("Received request to finalize match. matchId={}, localScore={}, visitorScore={}", matchId, result.getLocalScore(), result.getVisitorScore());
		return ResponseEntity.ok(MatchResponseMapper.toDTO(matchService.finalizeMatch(matchId, result)));
	}

}
