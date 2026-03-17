package com.escuela.techcup.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.escuela.techcup.controller.dto.PlayerResponseDTO;
import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.controller.mapper.PlayerMapper;
import com.escuela.techcup.core.exception.InvalidImageException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.service.PlayerService;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
	private static final Logger log = LoggerFactory.getLogger(PlayerController.class);

	private final PlayerService playerService;

	public PlayerController(PlayerService playerService) {
		this.playerService = playerService;
	}

	@PostMapping("/sports-profile")
	public ResponseEntity<PlayerResponseDTO> createSportsProfileStudent(@Valid @RequestBody StudentPlayerDTO studentPlayerDTO) {
		log.info("Request received to create player sports profile. mail={}, position={}, dorsal={}",
			studentPlayerDTO.getMail(), studentPlayerDTO.getPosition(), studentPlayerDTO.getDorsalNumber());
		log.debug("Creating sports profile without photo for mail={}", studentPlayerDTO.getMail());

		Player createdPlayer = playerService.createSportsProfile(studentPlayerDTO);

		log.info("Player sports profile created successfully. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}
	
	@PostMapping("/sports-profile/with-photo")
	public ResponseEntity<PlayerResponseDTO> createSportsProfileStudentWithPhoto(@Valid @RequestPart("player") StudentPlayerDTO studentPlayerDTO, @RequestPart("profilePicture") MultipartFile profilePicture) throws IOException {
		log.info("Request received to create player sports profile with photo. mail={}, position={}, dorsal={}, hasPhoto={}",
			studentPlayerDTO.getMail(), studentPlayerDTO.getPosition(), studentPlayerDTO.getDorsalNumber(), profilePicture != null && !profilePicture.isEmpty());
		log.debug("Reading profile picture bytes for mail={}", studentPlayerDTO.getMail());

		BufferedImage picture = ImageIO.read(profilePicture.getInputStream());
		if (picture == null) {
			log.warn("Invalid profile picture received for mail={}", studentPlayerDTO.getMail());
			throw new InvalidImageException("El archivo de imagen no es valido");
		}

		Player createdPlayer = playerService.createSportsProfile(
			studentPlayerDTO,
			picture
		);
		log.info("Player sports profile with photo created successfully. userId={}", createdPlayer.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(PlayerMapper.toResponseDTO(createdPlayer));
	}

}
