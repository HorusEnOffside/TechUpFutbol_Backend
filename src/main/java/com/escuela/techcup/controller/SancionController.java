package com.escuela.techcup.controller;

import com.escuela.techcup.controller.dto.CreateSancionDTO;
import com.escuela.techcup.core.util.IdGeneratorUtil;
import com.escuela.techcup.persistence.entity.tournament.SancionEntity;
import com.escuela.techcup.persistence.repository.tournament.SancionRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sanciones")
@Tag(name = "Sanciones", description = "Gestion de sanciones a equipos y jugadores")
public class SancionController {

    private static final Logger log = LoggerFactory.getLogger(SancionController.class);

    private final SancionRepository sancionRepository;

    public SancionController(SancionRepository sancionRepository) {
        this.sancionRepository = sancionRepository;
    }

    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<Map<String, String>> crearSancion(@Valid @RequestBody CreateSancionDTO dto) {
        log.info("Request to create sancion. tipo={}, entidadId={}", dto.getTipo(), dto.getEntidadId());

        SancionEntity sancion = new SancionEntity();
        sancion.setId(IdGeneratorUtil.generateId());
        sancion.setTipo(dto.getTipo());
        sancion.setEntidadId(dto.getEntidadId());
        sancion.setEntidadNombre(dto.getEntidadNombre());
        sancion.setMotivo(dto.getMotivo());
        sancion.setFecha(dto.getFecha());

        sancionRepository.save(sancion);
        log.info("Sancion created. id={}", sancion.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", sancion.getId()));
    }
}
