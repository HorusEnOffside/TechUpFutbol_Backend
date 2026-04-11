package com.escuela.techcup.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamFullInfoDTO {
    private String teamId;
    private String name;
    private String uniformColor;
    private String tournamentId;
    private String tournamentName;
    private String captainName;
    private List<TeamPlayerInfoDTO> players;
    private List<TeamMatchInfoDTO> matches;
    private TeamStatsDTO stats;
}