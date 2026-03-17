package com.escuela.techcup.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.controller.mapper.PlayerMapper;
import com.escuela.techcup.core.exception.InvalidImageException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.service.PlayerService;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
	private final PlayerService playerService;

	public PlayerController(PlayerService playerService) {
		this.playerService = playerService;
	}

	@PostMapping("/sports-profile")
	public ResponseEntity<PlayerDTO> createSportsProfileStudent(@Valid @RequestBody StudentPlayerDTO studentPlayerDTO) {
		Player createdPlayer = playerService.createSportsProfile(studentPlayerDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toDTO(createdPlayer));
	}

	
	@PostMapping("/sports-profile/with-photo")
	public ResponseEntity<PlayerDTO> createSportsProfileStudentWithPhoto(@Valid @RequestPart("player") StudentPlayerDTO studentPlayerDTO, @RequestPart("profilePicture") MultipartFile profilePicture) throws IOException {
		BufferedImage picture = ImageIO.read(profilePicture.getInputStream());
		if (picture == null) {
			throw new InvalidImageException("El archivo de imagen no es valido");
		}

		Player createdPlayer = playerService.createSportsProfile(
			studentPlayerDTO,
			picture
		);
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toDTO(createdPlayer));
	}

}
