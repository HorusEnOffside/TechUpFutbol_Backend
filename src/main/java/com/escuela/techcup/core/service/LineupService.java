package com.escuela.techcup.core.service;

import com.escuela.techcup.controller.dto.LineupRequestDTO;
import com.escuela.techcup.controller.dto.LineupResponseDTO;

public interface LineupService {

    LineupResponseDTO submitLineup(LineupRequestDTO request);

    LineupResponseDTO getLineup(String matchId, String teamId);

    LineupResponseDTO validateLineup(String lineupId);
}
