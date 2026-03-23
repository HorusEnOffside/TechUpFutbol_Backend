package com.escuela.techcup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/matches")
@Tag(name = "Gestion de partidos", description = "Operaciones de partidos")
public class MatchController {

	@GetMapping("/health")
	public String health() {
		return "Match controller OK";
	}

}
