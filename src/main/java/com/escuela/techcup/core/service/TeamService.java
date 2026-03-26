package com.escuela.techcup.core.service;

import com.escuela.techcup.controller.dto.TeamDTO;
import com.escuela.techcup.core.model.Team;

import java.awt.image.BufferedImage;

public interface TeamService {
    public Team createTeam(TeamDTO teamDTO, BufferedImage logo);
}
