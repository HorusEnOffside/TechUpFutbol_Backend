package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.Formation;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineupResponseDTO {
    private String id;
    private String matchId;
    private String teamId;
    private String teamName;
    private Formation formation;
    private String status;
    private List<LineupPlayerResponseDTO> players;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineupPlayerResponseDTO {
        private String playerId;
        private String playerName;
        private String position;
        private int dorsalNumber;
    }
}
