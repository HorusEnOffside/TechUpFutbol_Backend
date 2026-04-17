package com.escuela.techcup.core.service;

import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.MatchEvent;

import java.util.List;

public interface StandingsService {

    void updateStandingsTable(String tournamentId);

    List<Team> getStandingsTable(String tournamentId);

    List<Player> getTopScorers(String tournamentId);

    List<MatchEvent> getCardsHistory(String tournamentId, String playerOrTeamId);
}

