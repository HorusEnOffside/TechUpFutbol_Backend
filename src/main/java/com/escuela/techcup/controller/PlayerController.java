package com.escuela.techcup.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.escuela.techcup.controller.dto.EntitySearchResultDTO;
import com.escuela.techcup.controller.dto.PlayerUpdateDTO;
import com.escuela.techcup.controller.dto.GraduatePlayerDTO;
import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.controller.dto.PlayerResponseDTO;
import com.escuela.techcup.controller.dto.PlayerSearchDTO;
import com.escuela.techcup.controller.dto.PlayerSearchResultDTO;
import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.controller.dto.TeacherPlayerDTO;
import com.escuela.techcup.controller.mapper.PlayerMapper;
import com.escuela.techcup.core.exception.InvalidImageException;
import com.escuela.techcup.core.exception.TechcupException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.core.service.PlayerService;

@RestController
@RequestMapping("/api/players")
@Tag(name = "Gestion de jugadores", description = "Creacion y consulta de perfiles deportivos")
public class PlayerController {

	private static final Logger log = LoggerFactory.getLogger(PlayerController.class);

	private final PlayerService playerService;

	public PlayerController(PlayerService playerService) {
		this.playerService = playerService;
	}

	// ── Creación de perfiles deportivos ──────────────────────────────────

	@PreAuthorize("permitAll()")
	@PostMapping(value = "/students/sports-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PlayerResponseDTO> createSportsProfileStudent(
			@Valid
			@Parameter(description = "Student player data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = StudentPlayerDTO.class)))
			@RequestPart("player") StudentPlayerDTO studentPlayerDTO,
			@Parameter(description = "Optional profile image file", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(type = "string", format = "binary")))
			@RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
	) throws IOException {
		log.info("Request to create student sports profile. mail={}, position={}, dorsal={}, photo={}",
				studentPlayerDTO.getMail(), studentPlayerDTO.getPosition(),
				studentPlayerDTO.getDorsalNumber(), profilePicture != null && !profilePicture.isEmpty());

		BufferedImage picture = null;
		if (profilePicture != null && !profilePicture.isEmpty())
			picture = readProfilePictureOrThrow(profilePicture, studentPlayerDTO.getMail());

		Player createdPlayer = playerService.createSportsProfileStudent(studentPlayerDTO, picture);
		log.info("Student sports profile created. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}

	@PreAuthorize("permitAll()")
	@PostMapping(value = "/teachers/sports-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PlayerResponseDTO> createSportsProfileTeacher(
			@Valid
			@Parameter(description = "Teacher player data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TeacherPlayerDTO.class)))
			@RequestPart("player") TeacherPlayerDTO teacherPlayerDTO,
			@Parameter(description = "Optional profile image file", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(type = "string", format = "binary")))
			@RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
	) throws IOException {
		log.info("Request to create teacher sports profile. mail={}, position={}, dorsal={}, photo={}",
				teacherPlayerDTO.getMail(), teacherPlayerDTO.getPosition(),
				teacherPlayerDTO.getDorsalNumber(), profilePicture != null && !profilePicture.isEmpty());

		BufferedImage picture = null;
		if (profilePicture != null && !profilePicture.isEmpty())
			picture = readProfilePictureOrThrow(profilePicture, teacherPlayerDTO.getMail());

		Player createdPlayer = playerService.createSportsProfileTeacher(teacherPlayerDTO, picture);
		log.info("Teacher sports profile created. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}

	@PreAuthorize("permitAll()")
	@PostMapping(value = "/familiars/sports-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PlayerResponseDTO> createSportsProfileFamiliar(
			@Valid
			@Parameter(description = "Familiar player data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PlayerDTO.class)))
			@RequestPart("player") PlayerDTO playerDTO,
			@Parameter(description = "Optional profile image file", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(type = "string", format = "binary")))
			@RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
	) throws IOException {
		log.info("Request to create familiar sports profile. mail={}, position={}, dorsal={}, photo={}",
				playerDTO.getMail(), playerDTO.getPosition(),
				playerDTO.getDorsalNumber(), profilePicture != null && !profilePicture.isEmpty());

		BufferedImage picture = null;
		if (profilePicture != null && !profilePicture.isEmpty())
			picture = readProfilePictureOrThrow(profilePicture, playerDTO.getMail());

		Player createdPlayer = playerService.createSportsProfileFamiliar(playerDTO, picture);
		log.info("Familiar sports profile created. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}

	@PreAuthorize("permitAll()")
	@PostMapping(value = "/graduates/sports-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PlayerResponseDTO> createSportsProfileGraduate(
			@Valid
			@Parameter(description = "Graduate player data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GraduatePlayerDTO.class)))
			@RequestPart("player") GraduatePlayerDTO graduatePlayerDTO,
			@Parameter(description = "Optional profile image file", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(type = "string", format = "binary")))
			@RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
	) throws IOException {
		log.info("Request to create graduate sports profile. mail={}, position={}, dorsal={}, photo={}",
				graduatePlayerDTO.getMail(), graduatePlayerDTO.getPosition(),
				graduatePlayerDTO.getDorsalNumber(), profilePicture != null && !profilePicture.isEmpty());

		BufferedImage picture = null;
		if (profilePicture != null && !profilePicture.isEmpty())
			picture = readProfilePictureOrThrow(profilePicture, graduatePlayerDTO.getMail());

		Player createdPlayer = playerService.createSportsProfileGraduate(graduatePlayerDTO, picture);
		log.info("Graduate sports profile created. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}

	// ── Consultas ────────────────────────────────────────────────────────

	@PreAuthorize("permitAll()")
	@GetMapping("/find")
	public ResponseEntity<EntitySearchResultDTO> findPlayerByName(@RequestParam String name) {
		log.info("Request to find player by name={}", name);
		return playerService.findByNameContaining(name)
				.map(p -> ResponseEntity.ok(new EntitySearchResultDTO(p.getUserId(), p.getName())))
				.orElse(ResponseEntity.notFound().build());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<List<PlayerResponseDTO>> getAllPlayers() {
		log.info("Request received to list all players");
		List<PlayerResponseDTO> players = playerService.getAllPlayers().stream()
				.map(PlayerMapper::toResponseDTO)
				.toList();
		return ResponseEntity.ok(players);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{userId}")
	public ResponseEntity<PlayerResponseDTO> getPlayerByUserId(@PathVariable String userId) {
		log.info("Request received to get player by userId={}", userId);
		return ResponseEntity.ok(
				playerService.getPlayerByUserId(userId)
						.map(PlayerMapper::toResponseDTO)
						.orElse(null)
		);
	}

	// ── RF-15: Búsqueda de jugadores ─────────────────────────────────────

	/**
	 * Busca jugadores disponibles aplicando filtros opcionales.
	 * RN-15: Solo retorna jugadores con status AVAILABLE.
	 */
	@PreAuthorize("hasAnyRole('CAPTAIN', 'ADMIN', 'ORGANIZER')")
	@GetMapping("/search")
	public ResponseEntity<List<PlayerSearchResultDTO>> searchPlayers(
			@RequestParam(required = false) Position position,
			@RequestParam(required = false) Integer semester,
			@RequestParam(required = false) Integer age,
			@RequestParam(required = false) Gender gender,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String playerId
	) {
		log.info("Request to search players. position={}, semester={}, age={}, gender={}, name={}, playerId={}",
				position, semester, age, gender, name, playerId);

		PlayerSearchDTO filters = new PlayerSearchDTO();
		filters.setPosition(position);
		filters.setSemester(semester);
		filters.setAge(age);
		filters.setGender(gender);
		filters.setName(name);
		filters.setPlayerId(playerId);

		return ResponseEntity.ok(playerService.searchPlayers(filters));
	}

	// ── Actualizar perfil deportivo ──────────────────────────────────────

	@PreAuthorize("isAuthenticated()")
	@PutMapping("/{userId}")
	public ResponseEntity<PlayerResponseDTO> updateProfile(
			@PathVariable String userId,
			@RequestBody PlayerUpdateDTO dto) {
		log.info("Request to update player profile. userId={}", userId);
		return ResponseEntity.ok(PlayerMapper.toResponseDTO(playerService.updateProfile(userId, dto)));
	}

	// ── RF-04: Actualizar estado del jugador ─────────────────────────────

	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/{userId}/status")
	public ResponseEntity<PlayerResponseDTO> updateStatus(
			@PathVariable String userId,
			@RequestParam PlayerStatus status) {
		log.info("Request to update player status. userId={}, status={}", userId, status);
		return ResponseEntity.ok(PlayerMapper.toResponseDTO(playerService.updateStatus(userId, status)));
	}

	// ── Helper ───────────────────────────────────────────────────────────

	private BufferedImage readProfilePictureOrThrow(MultipartFile profilePicture, String mail) throws IOException {
		if (profilePicture == null || profilePicture.isEmpty()) {
			log.warn("Profile picture is missing for mail={}", mail);
			throw new InvalidImageException("Profile picture is required for this endpoint");
		}
		BufferedImage picture = ImageIO.read(profilePicture.getInputStream());
		if (picture == null) {
			log.warn("Invalid profile picture received for mail={}", mail);
			throw new InvalidImageException("Invalid image file");
		}
		return picture;
	}
}