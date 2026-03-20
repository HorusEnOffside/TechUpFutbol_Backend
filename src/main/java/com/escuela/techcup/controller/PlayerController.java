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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

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
public class PlayerController {
	private static final Logger log = LoggerFactory.getLogger(PlayerController.class);

	private final PlayerService playerService;

	public PlayerController(PlayerService playerService) {
		this.playerService = playerService;
	}

	@PreAuthorize("permitAll()")
	@PostMapping("/students/sports-profile")
	public ResponseEntity<PlayerResponseDTO> createSportsProfileStudent(@Valid @RequestBody StudentPlayerDTO studentPlayerDTO) {
		log.info("Request received to create student sports profile. mail={}, position={}, dorsal={}",
			studentPlayerDTO.getMail(), studentPlayerDTO.getPosition(), studentPlayerDTO.getDorsalNumber());
		log.debug("Creating student sports profile without photo for mail={}", studentPlayerDTO.getMail());

		Player createdPlayer = playerService.createSportsProfileStudent(studentPlayerDTO);

		log.info("Student sports profile created successfully. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}
	
	@PreAuthorize("permitAll()")
	@PostMapping(value = "/students/sports-profile/with-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PlayerResponseDTO> createSportsProfileStudentWithPhoto(
		@Valid
		@Parameter(description = "Student player data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = StudentPlayerDTO.class)))
		@RequestPart("player") StudentPlayerDTO studentPlayerDTO,
		@Parameter(description = "Profile image file", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(type = "string", format = "binary")))
		@RequestPart("profilePicture") MultipartFile profilePicture
	) throws IOException {
		log.info("Request received to create student sports profile with photo. mail={}, position={}, dorsal={}, hasPhoto={}",
			studentPlayerDTO.getMail(), studentPlayerDTO.getPosition(), studentPlayerDTO.getDorsalNumber(), profilePicture != null && !profilePicture.isEmpty());
		log.debug("Reading student profile picture bytes for mail={}", studentPlayerDTO.getMail());

		BufferedImage picture = readProfilePictureOrThrow(profilePicture, studentPlayerDTO.getMail());

		Player createdPlayer = playerService.createSportsProfileStudent(studentPlayerDTO,picture);
		log.info("Student sports profile with photo created successfully. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}

	@PreAuthorize("permitAll()")
	@PostMapping("/teachers/sports-profile")
	public ResponseEntity<PlayerResponseDTO> createSportsProfileTeacher(@Valid @RequestBody PlayerDTO playerDTO) {
		log.info("Request received to create teacher sports profile. mail={}, position={}, dorsal={}",
			playerDTO.getMail(), playerDTO.getPosition(), playerDTO.getDorsalNumber());
		log.debug("Creating teacher sports profile without photo for mail={}", playerDTO.getMail());

		Player createdPlayer = playerService.createSportsProfileTeacher(playerDTO);

		log.info("Teacher sports profile created successfully. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}

	@PreAuthorize("permitAll()")
	@PostMapping(value = "/teachers/sports-profile/with-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PlayerResponseDTO> createSportsProfileTeacherWithPhoto(
		@Valid
		@Parameter(description = "Teacher player data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PlayerDTO.class)))
		@RequestPart("player") PlayerDTO playerDTO,
		@Parameter(description = "Profile image file", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(type = "string", format = "binary")))
		@RequestPart("profilePicture") MultipartFile profilePicture
	) throws IOException {
		log.info("Request received to create teacher sports profile with photo. mail={}, position={}, dorsal={}, hasPhoto={}",
			playerDTO.getMail(), playerDTO.getPosition(), playerDTO.getDorsalNumber(), profilePicture != null && !profilePicture.isEmpty());
		log.debug("Reading teacher profile picture bytes for mail={}", playerDTO.getMail());

		BufferedImage picture = readProfilePictureOrThrow(profilePicture, playerDTO.getMail());

		Player createdPlayer = playerService.createSportsProfileTeacher(playerDTO, picture);
		log.info("Teacher sports profile with photo created successfully. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}

	@PreAuthorize("permitAll()")
	@PostMapping("/familiars/sports-profile")
	public ResponseEntity<PlayerResponseDTO> createSportsProfileFamiliar(@Valid @RequestBody PlayerDTO playerDTO) {
		log.info("Request received to create familiar sports profile. mail={}, position={}, dorsal={}",
			playerDTO.getMail(), playerDTO.getPosition(), playerDTO.getDorsalNumber());
		log.debug("Creating familiar sports profile without photo for mail={}", playerDTO.getMail());

		Player createdPlayer = playerService.createSportsProfileFamiliar(playerDTO);

		log.info("Familiar sports profile created successfully. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}

	@PreAuthorize("permitAll()")
	@PostMapping(value = "/familiars/sports-profile/with-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PlayerResponseDTO> createSportsProfileFamiliarWithPhoto(
		@Valid
		@Parameter(description = "Familiar player data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PlayerDTO.class)))
		@RequestPart("player") PlayerDTO playerDTO,
		@Parameter(description = "Profile image file", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(type = "string", format = "binary")))
		@RequestPart("profilePicture") MultipartFile profilePicture
	) throws IOException {
		log.info("Request received to create familiar sports profile with photo. mail={}, position={}, dorsal={}, hasPhoto={}",
			playerDTO.getMail(), playerDTO.getPosition(), playerDTO.getDorsalNumber(), profilePicture != null && !profilePicture.isEmpty());
		log.debug("Reading familiar profile picture bytes for mail={}", playerDTO.getMail());

		BufferedImage picture = readProfilePictureOrThrow(profilePicture, playerDTO.getMail());

		Player createdPlayer = playerService.createSportsProfileFamiliar(playerDTO, picture);
		log.info("Familiar sports profile with photo created successfully. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}

	@PreAuthorize("permitAll()")
	@PostMapping("/graduates/sports-profile")
	public ResponseEntity<PlayerResponseDTO> createSportsProfileGraduate(@Valid @RequestBody PlayerDTO playerDTO) {
		log.info("Request received to create graduate sports profile. mail={}, position={}, dorsal={}",
			playerDTO.getMail(), playerDTO.getPosition(), playerDTO.getDorsalNumber());
		log.debug("Creating graduate sports profile without photo for mail={}", playerDTO.getMail());

		Player createdPlayer = playerService.createSportsProfileGraduate(playerDTO);

		log.info("Graduate sports profile created successfully. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}

	@PreAuthorize("permitAll()")
	@PostMapping(value = "/graduates/sports-profile/with-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PlayerResponseDTO> createSportsProfileGraduateWithPhoto(
		@Valid
		@Parameter(description = "Graduate player data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PlayerDTO.class)))
		@RequestPart("player") PlayerDTO playerDTO,
		@Parameter(description = "Profile image file", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(type = "string", format = "binary")))
		@RequestPart("profilePicture") MultipartFile profilePicture
	) throws IOException {
		log.info("Request received to create graduate sports profile with photo. mail={}, position={}, dorsal={}, hasPhoto={}",
			playerDTO.getMail(), playerDTO.getPosition(), playerDTO.getDorsalNumber(), profilePicture != null && !profilePicture.isEmpty());
		log.debug("Reading graduate profile picture bytes for mail={}", playerDTO.getMail());

		BufferedImage picture = readProfilePictureOrThrow(profilePicture, playerDTO.getMail());

		Player createdPlayer = playerService.createSportsProfileGraduate(playerDTO, picture);
		log.info("Graduate sports profile with photo created successfully. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}

	@GetMapping
	public ResponseEntity<List<PlayerResponseDTO>> getAllPlayers() {
		log.info("Request received to list all players");
		List<PlayerResponseDTO> players = playerService.getAllPlayers().stream()
			.map(PlayerMapper::toResponseDTO)
			.toList();
		return ResponseEntity.ok(players);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<PlayerResponseDTO> getPlayerByUserId(@PathVariable String userId) {
		log.info("Request received to get player by userId={}", userId);
		PlayerResponseDTO player = playerService.getPlayerByUserId(userId)
			.map(PlayerMapper::toResponseDTO)
			.orElseThrow(() -> new TechcupException("Jugador no encontrado", HttpStatus.NOT_FOUND));
		return ResponseEntity.ok(player);
	}

	private BufferedImage readProfilePictureOrThrow(MultipartFile profilePicture, String mail) throws IOException {
		if (profilePicture == null || profilePicture.isEmpty()) {
			log.warn("Profile picture is missing for mail={}", mail);
			throw new InvalidImageException("La foto de perfil es obligatoria para este endpoint");
		}

		BufferedImage picture = ImageIO.read(profilePicture.getInputStream());
		if (picture == null) {
			log.warn("Invalid profile picture received for mail={}", mail);
			throw new InvalidImageException("El archivo de imagen no es valido");
		}

		return picture;
	}

	
}
