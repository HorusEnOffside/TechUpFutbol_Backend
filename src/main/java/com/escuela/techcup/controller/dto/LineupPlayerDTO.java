package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.Position;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineupPlayerDTO {
    @NotNull
    private String playerId;
    @NotNull
    private Position position;
    private int dorsalNumber;
}
