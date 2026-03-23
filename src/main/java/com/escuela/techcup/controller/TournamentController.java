package com.escuela.techcup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tournaments")
@Tag(name = "Gestion de torneos", description = "Operaciones de torneos")
public class TournamentController {

	@GetMapping("/health")
	public String health() {
		return "Tournament controller OK";
	}

}
