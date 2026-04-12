package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.model.EliminationBracket;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.Match;
import com.escuela.techcup.core.service.EliminationBracketService;
import com.escuela.techcup.core.service.StandingsService;
import com.escuela.techcup.core.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class EliminationBracketServiceImpl implements EliminationBracketService {

    @Autowired
    private StandingsService standingsService;

    @Autowired
    private MatchService matchService;

    @Override
    public EliminationBracket getBracketsForTournament(String tournamentId) {
        List<Match> matches = matchService.getAllMatches();
        boolean allFinished = matches.stream()
                .filter(m -> m.getTeamA() != null && m.getTeamA().getTournament() != null
                        && m.getTeamA().getTournament().getId().equals(tournamentId)
                        && m.getTeamB() != null && m.getTeamB().getTournament() != null
                        && m.getTeamB().getTournament().getId().equals(tournamentId))
                .allMatch(m -> "FINISHED".equalsIgnoreCase(m.getStatus()));
        if (!allFinished) {
            return null;
        }
        List<Team> standings = standingsService.getStandingsTable(tournamentId);
        if (standings == null || standings.isEmpty()) {
            return null;
        }
        int numTeams = standings.size();
        EliminationBracket bracket = new EliminationBracket();
        if (numTeams >= 16) {
            bracket.setRoundOf16(generatePairs(standings, 16));
        }
        if (numTeams >= 8) {
            bracket.setQuarterFinals(generatePairs(standings, 8));
        }
        if (numTeams >= 4) {
            bracket.setSemiFinals(generatePairs(standings, 4));
        }
        if (numTeams == 2) {
            bracket.setFinals(generatePairs(standings, 2));
        }
        return bracket;
    }

    private List<EliminationBracket.MatchPair> generatePairs(List<Team> teams, int numPairs) {
        List<EliminationBracket.MatchPair> pairs = new ArrayList<>();
        for (int i = 0; i < numPairs / 2; i++) {
            EliminationBracket.MatchPair pair = new EliminationBracket.MatchPair();
            pair.setTeam1(teams.get(i));
            pair.setTeam2(teams.get(numPairs - 1 - i));
            pairs.add(pair);
        }
        return pairs;
    }
}
