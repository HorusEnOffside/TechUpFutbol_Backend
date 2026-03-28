package com.escuela.techcup.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.http.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.escuela.techcup.controller.dto.UserDTO;
import com.escuela.techcup.controller.dto.UserResponseDTO;
import com.escuela.techcup.controller.mapper.UserMapper;
import com.escuela.techcup.core.exception.InvalidImageException;
import com.escuela.techcup.core.exception.TechcupException;
import com.escuela.techcup.core.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Gestion de usuarios", description = "Administracion y consulta de usuarios")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/admin", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDTO> createAdminUser(
        @Valid
        @Parameter(description = "Admin user data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class)))
        @RequestPart("user") UserDTO userDTO,
        @Parameter(description = "Optional profile image file", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(type = "string", format = "binary")))
        @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
    ) throws IOException {
        BufferedImage picture = null;
		if (profilePicture != null && !profilePicture.isEmpty()) {
			picture = readProfilePictureOrThrow(profilePicture, userDTO.getMail());
		}
        log.info("Request received to create admin user. mail={}", userDTO.getMail());
        UserResponseDTO response = UserMapper.toResponseDTO(userService.createAdminUser(userDTO, picture));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/organizer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDTO> createOrganizerUser(
        @Valid
        @Parameter(description = "Organizer user data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class)))
        @RequestPart("user") UserDTO userDTO,
        @Parameter(description = "Optional profile image file", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(type = "string", format = "binary")))
        @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
    ) throws IOException {
        BufferedImage picture = null;
		if (profilePicture != null && !profilePicture.isEmpty()) {
			picture = readProfilePictureOrThrow(profilePicture, userDTO.getMail());
		}
        log.info("Request received to create organizer user. mail={}", userDTO.getMail());
        UserResponseDTO response = UserMapper.toResponseDTO(userService.createOrganizerUser(userDTO, picture));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/referee", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDTO> createRefereeUser(
        @Valid
        @Parameter(description = "Referee user data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class)))
        @RequestPart("user") UserDTO userDTO,
        @Parameter(description = "Optional profile image file", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(type = "string", format = "binary")))
        @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
    ) throws IOException {
        BufferedImage picture = null;
		if (profilePicture != null && !profilePicture.isEmpty()) {
			picture = readProfilePictureOrThrow(profilePicture, userDTO.getMail());
		}
        log.info("Request received to create referee user. mail={}", userDTO.getMail());
        UserResponseDTO response = UserMapper.toResponseDTO(userService.createRefereeUser(userDTO, picture));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("Request received to list all users");
        List<UserResponseDTO> users = userService.getAllUsers().stream()
                .map(UserMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        log.info("Request received to get user by id={}", id);
        UserResponseDTO user = userService.getUserById(id)
                .map(UserMapper::toResponseDTO)
                .orElseThrow(() -> new TechcupException("User not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(user);
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