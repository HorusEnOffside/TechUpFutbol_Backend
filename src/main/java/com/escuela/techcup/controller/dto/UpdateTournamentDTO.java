package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.TournamentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UpdateTournamentDTO {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer teamsMaxAmount;
    private Double teamCost;
    private TournamentStatus status;
}