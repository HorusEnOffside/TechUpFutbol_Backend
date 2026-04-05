package com.escuela.techcup.controller;

import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.escuela.techcup.core.model.Match;
import com.escuela.techcup.core.service.MatchService;

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
	public ResponseEntity<Match> getMatchById(@PathVariable String id) {
		log.info("Received request to get match by id: {}", id);
		return ResponseEntity.ok(matchService.getMatchById(id));
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN') or hasRole('REFEREE') or hasRole('USER')")
	public ResponseEntity<List<Match>> getAllMatches() {
		log.info("Received request to get all matches");
		return ResponseEntity.ok(matchService.getAllMatches());
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
	public ResponseEntity<Match> createMatch(
			@RequestParam LocalDate date,
			@RequestParam String teamAId,
			@RequestParam String teamBId) {
		log.info("Received request to create match: date={}, teamAId={}, teamBId={}", date, teamAId, teamBId);
		Match match = matchService.createMatch(date, teamAId, teamBId);
		return ResponseEntity.status(201).body(match);
	}

	@PutMapping("/{matchId}/referee")
	@PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
	public ResponseEntity<Match> setReferee(
			@PathVariable String matchId,
			@RequestParam String refereeId) {
		log.info("Received request to set referee for match. matchId={}, refereeId={}", matchId, refereeId);
		return ResponseEntity.ok(matchService.setReferee(matchId, refereeId));
	}

	@PostMapping("/{matchId}/soccer-field")
	@PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
	public ResponseEntity<Match> setSoccerField(
			@PathVariable String matchId,
			@RequestParam String soccerFieldId) {
		log.info("Received request to set soccer field for match. matchId={}, soccerFieldId={}", matchId, soccerFieldId);
		return ResponseEntity.ok(matchService.setSoccerField(matchId, soccerFieldId));
	}

	@PostMapping("/{matchId}/events/goal")
	@PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER') or hasRole('REFEREE')")
	public ResponseEntity<Match> addMatchEventGoal(
			@PathVariable String matchId,
			@RequestParam String playerId,
			@RequestParam int minute,
			@RequestParam String description) {
		log.info("Received request to add goal event to match. matchId={}, playerId={}, minute={}, description={}", matchId, playerId, minute, description);
		return ResponseEntity.ok(matchService.addMatchEventGoal(matchId, playerId, minute, description));
	}

}
