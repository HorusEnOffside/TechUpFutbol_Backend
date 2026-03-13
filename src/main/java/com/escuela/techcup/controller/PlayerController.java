package com.escuela.techcup.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.escuela.techcup.model.Player;
import com.escuela.techcup.model.enums.Gender;
import com.escuela.techcup.model.enums.PlayerType;
import com.escuela.techcup.model.enums.Position;
import com.escuela.techcup.service.PlayerService;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
	private final PlayerService playerService;

	public PlayerController(PlayerService playerService) {
		this.playerService = playerService;
	}

	@PostMapping("/sports-profile")
	public ResponseEntity<Player> createSportsProfile(
		@RequestParam String id,
		@RequestParam String name,
		@RequestParam String email,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
		@RequestParam Gender gender,
		@RequestParam PlayerType playerType,
		@RequestParam int dorsalNumber,
		@RequestParam Position position,
		@RequestParam String password
	) {
		Player createdPlayer = playerService.createSportsProfile(
			id,
			name,
			email,
			dateOfBirth,
			gender,
			playerType,
			dorsalNumber,
			position,
			password
		);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdPlayer);
	}

	@PostMapping(value = "/sports-profile/with-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Player> createSportsProfileWithPhoto(
		@RequestParam String id,
		@RequestParam String name,
		@RequestParam String email,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
		@RequestParam Gender gender,
		@RequestParam PlayerType playerType,
		@RequestParam int dorsalNumber,
		@RequestParam Position position,
		@RequestParam MultipartFile profilePicture,
		@RequestParam String password
	) throws IOException {
		BufferedImage picture = ImageIO.read(profilePicture.getInputStream());
		if (picture == null) {
			throw new IllegalArgumentException("El archivo de imagen no es valido");
		}

		Player createdPlayer = playerService.createSportsProfile(
			id,
			name,
			email,
			dateOfBirth,
			gender,
			playerType,
			dorsalNumber,
			position,
			picture,
			password
		);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdPlayer);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleValidationError(IllegalArgumentException ex) {
		return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<Map<String, String>> handleImageError(IOException ex) {
		return ResponseEntity.badRequest().body(Map.of("error", "No se pudo procesar la imagen"));
	}

}
