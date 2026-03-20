package com.escuela.techcup.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.escuela.techcup.controller.dto.UserDTO;
import com.escuela.techcup.controller.dto.UserResponseDTO;
import com.escuela.techcup.controller.mapper.UserMapper;
import com.escuela.techcup.core.exception.TechcupException;
import com.escuela.techcup.core.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/admin")
	public ResponseEntity<UserResponseDTO> createAdminUser(@Valid @RequestBody UserDTO userDTO) {
		log.info("Request received to create admin user. mail={}", userDTO.getMail());
		log.debug("Creating admin user for mail={}", userDTO.getMail());

		UserResponseDTO response = UserMapper.toResponseDTO(userService.createAdminUser(userDTO));
		log.info("Admin user created successfully. mail={}", response.getMail());

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/organizer")
	public ResponseEntity<UserResponseDTO> createOrganizerUser(@Valid @RequestBody UserDTO userDTO) {
		log.info("Request received to create organizer user. mail={}", userDTO.getMail());
		log.debug("Creating organizer user for mail={}", userDTO.getMail());

		UserResponseDTO response = UserMapper.toResponseDTO(userService.createOrganizerUser(userDTO));
		log.info("Organizer user created successfully. mail={}", response.getMail());

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/referee")
	public ResponseEntity<UserResponseDTO> createRefereeUser(@Valid @RequestBody UserDTO userDTO) {
		log.info("Request received to create referee user. mail={}", userDTO.getMail());
		log.debug("Creating referee user for mail={}", userDTO.getMail());

		UserResponseDTO response = UserMapper.toResponseDTO(userService.createRefereeUser(userDTO));
		log.info("Referee user created successfully. mail={}", response.getMail());

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
			.orElseThrow(() -> new TechcupException("Usuario no encontrado", HttpStatus.NOT_FOUND));
		return ResponseEntity.ok(user);
	}
}
