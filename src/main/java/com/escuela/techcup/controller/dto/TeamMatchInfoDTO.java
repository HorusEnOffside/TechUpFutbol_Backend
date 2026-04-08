package com.escuela.techcup.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamMatchInfoDTO {
    private String matchId;
    private LocalDateTime dateTime;
    private String opponentName;
    private int goalsFor;
    private int goalsAgainst;
    private String result; // WIN, LOSS, DRAW, PENDING
}