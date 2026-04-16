package com.escuela.techcup.controller.mapper;

import com.escuela.techcup.controller.dto.MatchResponseDTO;
import com.escuela.techcup.controller.dto.MatchResponseDTO.GoalResponseDTO;
import com.escuela.techcup.controller.dto.MatchResponseDTO.RefereeSummaryDTO;
import com.escuela.techcup.controller.dto.MatchResponseDTO.SoccerFieldResponseDTO;
import com.escuela.techcup.controller.dto.MatchResponseDTO.TeamSummaryDTO;
import com.escuela.techcup.core.model.Goal;
import com.escuela.techcup.core.model.Match;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class MatchResponseMapper {

    private MatchResponseMapper() {}

    public static MatchResponseDTO toDTO(Match match) {
        if (match == null) return null;

        TeamSummaryDTO teamA = match.getTeamA() != null
                ? new TeamSummaryDTO(match.getTeamA().getId(), match.getTeamA().getName(), match.getTeamA().getUniformColor())
                : null;

        TeamSummaryDTO teamB = match.getTeamB() != null
                ? new TeamSummaryDTO(match.getTeamB().getId(), match.getTeamB().getName(), match.getTeamB().getUniformColor())
                : null;

        RefereeSummaryDTO referee = match.getReferee() != null
                ? new RefereeSummaryDTO(match.getReferee().getId(), match.getReferee().getName(), match.getReferee().getMail())
                : null;

        SoccerFieldResponseDTO soccerField = null;
        if (match.getSoccerField() != null) {
            String fotoBase64 = match.getSoccerField().getFoto() != null
                    ? Base64.getEncoder().encodeToString(match.getSoccerField().getFoto())
                    : null;
            soccerField = new SoccerFieldResponseDTO(
                    match.getSoccerField().getId(),
                    match.getSoccerField().getName(),
                    match.getSoccerField().getLocation(),
                    fotoBase64);
        }

        List<GoalResponseDTO> goals = Collections.emptyList();
        if (match.getEvents() != null) {
            goals = match.getEvents().stream()
                    .filter(e -> e instanceof Goal)
                    .map(e -> {
                        Goal g = (Goal) e;
                        String playerName = g.getPlayer() != null && g.getPlayer().getUserPlayer() != null
                            ? g.getPlayer().getUserPlayer().getName() : null;
                        return new GoalResponseDTO(g.getMinute(), playerName, g.getDescription());
                    })
                    .toList();
        }

        return new MatchResponseDTO(
                match.getId(),
                match.getDateTime(),
                match.getStatus(),
                teamA,
                teamB,
                referee,
                soccerField,
                goals);
    }
}
