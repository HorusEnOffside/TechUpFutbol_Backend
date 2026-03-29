package com.escuela.techcup.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.escuela.techcup.controller.dto.UserResponseDTO;
import com.escuela.techcup.controller.mapper.UserMapper;
import com.escuela.techcup.core.exception.TechcupException;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.core.service.AdminService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administracion", description = "Operaciones exclusivas del administrador")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<UserResponseDTO> assignRole(
            @PathVariable String userId,
            @RequestParam UserRole role) {
        log.info("Request to assign role={} to userId={}", role, userId);
        UserResponseDTO response = UserMapper.toResponseDTO(adminService.assignRole(userId, role));
        return ResponseEntity.ok(response);
    }
}