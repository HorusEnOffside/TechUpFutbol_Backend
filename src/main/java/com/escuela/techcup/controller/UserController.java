package com.escuela.techcup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.escuela.techcup.controller.dto.UserDTO;
import com.escuela.techcup.controller.dto.UserResponseDTO;
import com.escuela.techcup.controller.mapper.UserMapper;
import com.escuela.techcup.core.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/admin")
	public ResponseEntity<UserResponseDTO> createAdminUser(@Valid @RequestBody UserDTO userDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResponseDTO(userService.createAdminUser(userDTO)));
	}

	@PostMapping("/organizer")
	public ResponseEntity<UserResponseDTO> createOrganizerUser(@Valid @RequestBody UserDTO userDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResponseDTO(userService.createOrganizerUser(userDTO)));
	}

	@PostMapping("/referee")
	public ResponseEntity<UserResponseDTO> createRefereeUser(@Valid @RequestBody UserDTO userDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResponseDTO(userService.createRefereeUser(userDTO)));
	}
}
