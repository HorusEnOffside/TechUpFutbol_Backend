package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.TournamentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CreateTournamentDTO {

    @NotNull(message = "startDate is required")
    private LocalDateTime startDate;

    @NotNull(message = "endDate is required")
    private LocalDateTime endDate;

    @Min(value = 2, message = "teamsMaxAmount must be at least 2")
    private int teamsMaxAmount;

    @NotNull(message = "teamCost is required")
    @Positive(message = "teamCost must be positive")
    private Double teamCost;

    @NotNull(message = "status is required")
    private TournamentStatus status;

    @NotNull(message = "organizerId is required")
    private String organizerId;
}