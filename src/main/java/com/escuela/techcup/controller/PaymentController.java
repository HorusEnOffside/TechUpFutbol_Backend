package com.escuela.techcup.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Gestion de pagos", description = "Operaciones de pagos")
public class PaymentController {

	@PreAuthorize("hasRole('ORGANIZER')")
	@GetMapping("/health")
	public String health() {
		return "Payment controller OK";
	}

}
