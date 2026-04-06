package com.escuela.techcup.controller;

import com.escuela.techcup.controller.dto.FormationDTO;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.enums.Formation;
import com.escuela.techcup.core.model.enums.InvitationStatus;
import com.escuela.techcup.core.service.TeamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
@Tag(name = "Gestion de equipos", description = "Operaciones de equipos")
public class TeamController {

	private static final Logger log = LoggerFactory.getLogger(TeamController.class);
	private final TeamService teamService;

	public TeamController(TeamService teamService) {
		this.teamService = teamService;
	}

	@PreAuthorize("hasAnyRole('CAPTAIN', 'ADMIN')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Team> createTeam(
			@RequestParam String name,
			@RequestParam String uniformColors,
			@RequestParam String captainUserId,
			@RequestPart(value = "logo", required = false) MultipartFile logo
	) throws IOException {
		log.info("Request to create team. name={}", name);
		BufferedImage logoImage = null;
		if (logo != null && !logo.isEmpty()) {
			logoImage = ImageIO.read(logo.getInputStream());
		}
		Team team = teamService.createTeam(name, uniformColors, logoImage, captainUserId);
		return ResponseEntity.status(HttpStatus.CREATED).body(team);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<List<Team>> getAllTeams() {
		log.info("Request to get all teams");
		return ResponseEntity.ok(teamService.getAllTeams());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{teamId}")
	public ResponseEntity<Team> getTeamById(@PathVariable String teamId) {
		log.info("Request to get team by id={}", teamId);
		return ResponseEntity.ok(teamService.getTeamById(teamId));
	}

	@PreAuthorize("hasAnyRole('CAPTAIN', 'ADMIN')")
	@PostMapping("/{teamId}/invite/{playerId}")
	public ResponseEntity<Void> invitePlayer(
			@PathVariable String teamId,
			@PathVariable String playerId,
			@RequestParam(required = false) String message
	) {
		log.info("Request to invite player. teamId={}, playerId={}", teamId, playerId);
		teamService.invitePlayer(teamId, playerId, message);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasAnyRole('PLAYER', 'CAPTAIN', 'BASEUSER')")
	@PutMapping("/invitations/{invitationId}")
	public ResponseEntity<Void> respondInvitation(
			@PathVariable String invitationId,
			@RequestParam InvitationStatus action
	) {
		log.info("Request to respond invitation. invitationId={}, action={}", invitationId, action);
		teamService.respondInvitation(invitationId, action);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasAnyRole('CAPTAIN', 'ORGANIZER', 'ADMIN')")
	@GetMapping("/{teamId}/validate/composition")
	public ResponseEntity<Boolean> validateTeamComposition(@PathVariable String teamId) {
		log.info("Request to validate team composition. teamId={}", teamId);
		return ResponseEntity.ok(teamService.validateTeamComposition(teamId));
	}

	@PreAuthorize("hasAnyRole('CAPTAIN', 'ORGANIZER', 'ADMIN')")
	@GetMapping("/{teamId}/validate/engineering")
	public ResponseEntity<Boolean> validateEngineeringMajority(@PathVariable String teamId) {
		log.info("Request to validate engineering majority. teamId={}", teamId);
		return ResponseEntity.ok(teamService.validateEngineeringMajority(teamId));
	}

	@PreAuthorize("hasAnyRole('CAPTAIN', 'ORGANIZER', 'ADMIN')")
	@GetMapping("/validate/player/{playerId}/tournament/{tournamentId}")
	public ResponseEntity<Boolean> validatePlayerUniquePerTournament(
			@PathVariable String playerId,
			@PathVariable String tournamentId
	) {
		log.info("Request to validate player unique per tournament. playerId={}, tournamentId={}", playerId, tournamentId);
		return ResponseEntity.ok(teamService.validatePlayerUniquePerTournament(playerId, tournamentId));
	}

    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMIN')")
    @PatchMapping("/{teamId}/formation")
    public ResponseEntity<Void> changeFormation(
            @PathVariable String teamId,
            @Valid @RequestBody FormationDTO dto) {

        log.info("Request to change formation. teamId={}, matchId={}, formation={}",
                teamId, dto.getMatchId(), dto.getFormation());

        teamService.changeFormation(dto.getFormation(), teamId, dto.getMatchId());

        log.info("Formation change request processed. teamId={}, matchId={}",
                teamId, dto.getMatchId());

        return ResponseEntity.noContent().build();
    }
}