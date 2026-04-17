package com.escuela.techcup.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResultDTO {
    private int localScore;
    private int visitorScore;
    private List<PlayerMatchStatsDTO> playerStats;
}
