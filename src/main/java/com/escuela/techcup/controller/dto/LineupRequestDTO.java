package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.Formation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineupRequestDTO {
    @NotNull
    private String matchId;
    @NotNull
    private String teamId;
    @NotNull
    private Formation formation;
    @NotNull
    private List<LineupPlayerDTO> players;
}
