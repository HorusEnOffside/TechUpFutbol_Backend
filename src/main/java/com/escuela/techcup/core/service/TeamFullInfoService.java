package com.escuela.techcup.core.service;

import com.escuela.techcup.controller.dto.TeamFullInfoDTO;
import java.util.List;

public interface TeamFullInfoService {
    TeamFullInfoDTO getTeamFullInfo(String teamId);
    List<TeamFullInfoDTO> getTeamsByTournament(String tournamentId);
}