package com.escuela.techcup.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.controller.dto.PlayerResponseDTO;
import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.controller.mapper.PlayerMapper;
import com.escuela.techcup.core.exception.InvalidImageException;
import com.escuela.techcup.core.exception.TechcupException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.service.PlayerService;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/players")
@Tag(name = "Gestion de jugadores", description = "Creacion y consulta de perfiles deportivos")
public class PlayerController {
	private static final Logger log = LoggerFactory.getLogger(PlayerController.class);

	private final PlayerService playerService;

	public PlayerController(PlayerService playerService) {
		this.playerService = playerService;
	}

	@PreAuthorize("permitAll()")
	@PostMapping(value = "/students/sports-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PlayerResponseDTO> createSportsProfileStudent(
		@Valid
		@Parameter(description = "Student player data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = StudentPlayerDTO.class)))
		@RequestPart("player") StudentPlayerDTO studentPlayerDTO,
		@Parameter(description = "Optional profile image file", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(type = "string", format = "binary")))
		@RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
	) throws IOException {
		log.info("Request to create student sports profile. mail={}, position={}, dorsal={}, Photo={}",
			studentPlayerDTO.getMail(), studentPlayerDTO.getPosition(), studentPlayerDTO.getDorsalNumber(), 
			profilePicture != null && !profilePicture.isEmpty());
		
		BufferedImage picture = null;
		if (profilePicture != null && !profilePicture.isEmpty()) {
			picture = readProfilePictureOrThrow(profilePicture, studentPlayerDTO.getMail());
		}

		Player createdPlayer = playerService.createSportsProfileStudent(studentPlayerDTO, picture);
		log.info("Student sports profile created. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}

	@PreAuthorize("permitAll()")
	@PostMapping(value = "/teachers/sports-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PlayerResponseDTO> createSportsProfileTeacher(
		@Valid
		@Parameter(description = "Teacher player data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PlayerDTO.class)))
		@RequestPart("player") PlayerDTO playerDTO,
		@Parameter(description = "Optional profile image file", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(type = "string", format = "binary")))
		@RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
	) throws IOException {
		log.info("Request to create teacher sports profile. mail={}, position={}, dorsal={}, Photo={}",
			playerDTO.getMail(), playerDTO.getPosition(), playerDTO.getDorsalNumber(), 
			profilePicture != null && !profilePicture.isEmpty());
		
		BufferedImage picture = null;
		if (profilePicture != null && !profilePicture.isEmpty()) {
			picture = readProfilePictureOrThrow(profilePicture, playerDTO.getMail());
		}

		Player createdPlayer = playerService.createSportsProfileTeacher(playerDTO, picture);
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
		log.info("Request to create familiar sports profile. mail={}, position={}, dorsal={}, Photo={}",
			playerDTO.getMail(), playerDTO.getPosition(), playerDTO.getDorsalNumber(), 
			profilePicture != null && !profilePicture.isEmpty());
		
		BufferedImage picture = null;
		if (profilePicture != null && !profilePicture.isEmpty()) {
			picture = readProfilePictureOrThrow(profilePicture, playerDTO.getMail());
		}

		Player createdPlayer = playerService.createSportsProfileFamiliar(playerDTO, picture);
		log.info("Familiar sports profile created. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}

	@PreAuthorize("permitAll()")
	@PostMapping(value = "/graduates/sports-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PlayerResponseDTO> createSportsProfileGraduate(
		@Valid
		@Parameter(description = "Graduate player data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PlayerDTO.class)))
		@RequestPart("player") PlayerDTO playerDTO,
		@Parameter(description = "Optional profile image file", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(type = "string", format = "binary")))
		@RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
	) throws IOException {
		log.info("Request to create graduate sports profile. mail={}, position={}, dorsal={}, Photo={}",
			playerDTO.getMail(), playerDTO.getPosition(), playerDTO.getDorsalNumber(), 
			profilePicture != null && !profilePicture.isEmpty());
		
		BufferedImage picture = null;
		if (profilePicture != null && !profilePicture.isEmpty()) {
			picture = readProfilePictureOrThrow(profilePicture, playerDTO.getMail());
		}

		Player createdPlayer = playerService.createSportsProfileGraduate(playerDTO, picture);
		log.info("Graduate sports profile created. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
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
		PlayerResponseDTO player = playerService.getPlayerByUserId(userId)
			.map(PlayerMapper::toResponseDTO)
			.orElseThrow(() -> new TechcupException("Player not found", HttpStatus.NOT_FOUND));
		return ResponseEntity.ok(player);
	}

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
