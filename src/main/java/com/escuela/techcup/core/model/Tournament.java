package com.escuela.techcup.core.model;

import java.time.LocalDateTime;

import com.escuela.techcup.core.model.enums.TournamentStatus;
import lombok.Data;

@Data
public class Tournament {
    private String id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime closingDate;
    private int teamsMaxAmount;
    private Double teamCost;
    private TournamentStatus status;
    private String reglamento;
    private String canchas;
    private String horarios;
    private String sanciones;
}