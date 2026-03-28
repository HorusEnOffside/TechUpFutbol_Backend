package com.escuela.techcup.core.model;

import java.time.LocalDateTime;

import com.escuela.techcup.core.model.enums.TournamentStatus;
import lombok.Data;

@Data
public class Tournament {
    private String id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int teamsMaxAmount;
    private Double teamCost;
    private TournamentStatus status;
    
}
