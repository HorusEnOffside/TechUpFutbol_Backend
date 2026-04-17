package com.escuela.techcup.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamStatsDTO {
    private int matchesPlayed;
    private int wins;
    private int losses;
    private int draws;
    private int goalsFor;
    private int goalsAgainst;
}