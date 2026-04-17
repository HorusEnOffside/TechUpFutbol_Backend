package com.escuela.techcup.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerMatchStatsDTO {
    private String playerId;
    private int goals;
    private int yellowCards;
    private int redCards;
}
