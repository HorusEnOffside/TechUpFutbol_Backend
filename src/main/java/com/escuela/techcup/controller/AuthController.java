package com.escuela.techcup.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.escuela.techcup.controller.dto.LoginRequest;
import com.escuela.techcup.controller.dto.LoginResponse;
import com.escuela.techcup.core.exception.TechcupException;
import com.escuela.techcup.core.model.User;
import com.escuela.techcup.core.service.JwtService;
import com.escuela.techcup.core.service.UserService;
import jakarta.validation.Valid;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autentication", description = "Inicio de sesion y emision de token")
public class AuthController {
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	private final UserService userService;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;

	public AuthController(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
	}



	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		log.info("Login request received for email={}", loginRequest.getEmail());

		User user = userService.getUserByMail(loginRequest.getEmail())
			.orElseThrow(() -> {
				log.warn("Login failed: user not found for email={}", loginRequest.getEmail());
				return new TechcupException("Invalid email or password", HttpStatus.UNAUTHORIZED);
			});

		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			log.warn("Login failed: invalid password for email={}", loginRequest.getEmail());
			throw new TechcupException("Invalid email or password", HttpStatus.UNAUTHORIZED);
		}

		String rolesAsString = user.getRoles().stream()
			.map(Enum::name)
			.collect(Collectors.joining(","));

		String token = jwtService.generateToken(user.getId(), user.getMail(),
			user.getRoles().stream()
				.map(Enum::name)
				.collect(Collectors.toSet()));

		log.info("Login successful for email={}, roles={}", loginRequest.getEmail(), rolesAsString);

		LoginResponse response = new LoginResponse(token, user.getId(), user.getMail(), user.getRoles());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/refresh")
	public ResponseEntity<LoginResponse> refresh(
			@RequestHeader("Authorization") String authHeader) {
		log.info("Refresh token request received");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new TechcupException("Token invalido o no proporcionado", HttpStatus.UNAUTHORIZED);
		}

		String token = authHeader.substring(7);

		if (!jwtService.isTokenValid(token)) {
			log.warn("Refresh token failed: invalid token");
			throw new TechcupException("Token invalido o expirado", HttpStatus.UNAUTHORIZED);
		}

		String userId = jwtService.extractUserId(token);

		User user = userService.getUserById(userId)
				.orElseThrow(() -> new TechcupException("User not found", HttpStatus.UNAUTHORIZED));

		Set<String> freshRoles = user.getRoles().stream()
				.map(Enum::name)
				.collect(Collectors.toSet());

		String newToken = jwtService.generateToken(userId, user.getMail(), freshRoles);
		log.info("Token refreshed successfully for userId={}, roles={}", userId, freshRoles);

		return ResponseEntity.ok(new LoginResponse(newToken, userId, user.getMail(), user.getRoles()));
	}
}
