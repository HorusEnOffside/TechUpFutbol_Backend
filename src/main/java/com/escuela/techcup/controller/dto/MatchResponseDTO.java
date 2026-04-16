package com.escuela.techcup.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponseDTO {

    private String id;
    private LocalDateTime dateTime;
    private String status;
    private TeamSummaryDTO teamA;
    private TeamSummaryDTO teamB;
    private RefereeSummaryDTO referee;
    private SoccerFieldResponseDTO soccerField;
    private List<GoalResponseDTO> goals;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamSummaryDTO {
        private String id;
        private String name;
        private String uniformColor;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefereeSummaryDTO {
        private String id;
        private String name;
        private String mail;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SoccerFieldResponseDTO {
        private String id;
        private String name;
        private String location;
        private String foto; // base64
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoalResponseDTO {
        private int minute;
        private String playerName;
        private String description;
    }
}
