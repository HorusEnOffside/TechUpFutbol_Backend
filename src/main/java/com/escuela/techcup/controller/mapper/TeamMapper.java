package com.escuela.techcup.controller.mapper;

import com.escuela.techcup.controller.dto.TeamResponseDTO;
import com.escuela.techcup.controller.dto.UserResponseDTO;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.User;

import java.util.EnumSet;

public class TeamMapper {

    private TeamMapper() {
    }

    public static TeamResponseDTO toResponseDTO(Team team) {
        return new TeamResponseDTO(
                team.getName(),
                team.getFormation(),
                team.getPlayers(),
                team.getUniformColors()
        );
    }
}
