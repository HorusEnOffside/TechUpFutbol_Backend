package com.escuela.techcup.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ConfigureTournamentDTO {

    @NotBlank(message = "reglamento is required")
    @Size(max = 2000, message = "reglamento must not exceed 2000 characters")
    private String reglamento;

    @NotNull(message = "closingDate is required")
    private LocalDateTime closingDate;

    @NotEmpty(message = "canchas is required")
    @Valid
    private List<CanchaDTO> canchas;

    @NotEmpty(message = "horarios is required")
    @Valid
    private List<HorarioDTO> horarios;

    private String sanciones;
}