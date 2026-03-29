package com.escuela.techcup.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/teams")
@Tag(name = "Gestion de equipos", description = "Operaciones de equipos")
public class TeamController {

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/health")
	public String health() {
		return "Team controller OK";
	}

}
