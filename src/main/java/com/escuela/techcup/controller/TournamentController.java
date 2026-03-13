package com.escuela.techcup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

	@GetMapping("/health")
	public String health() {
		return "Tournament controller OK";
	}

}
