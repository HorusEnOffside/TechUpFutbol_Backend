package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.TournamentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TournamentResponseDTO {

    private String id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime closingDate;
    private int teamsMaxAmount;
    private Double teamCost;
    private TournamentStatus status;
    private String reglamento;
    private String sanciones;
    private List<CanchaResponseDTO> canchas;
    private List<HorarioResponseDTO> horarios;

    @Data
    public static class CanchaResponseDTO {
        private String id;
        private String tipo;
        private String nombre;
        private String fotoUrl;
    }

    @Data
    public static class HorarioResponseDTO {
        private String id;
        private LocalDate fecha;
        private String descripcion;
    }
}
