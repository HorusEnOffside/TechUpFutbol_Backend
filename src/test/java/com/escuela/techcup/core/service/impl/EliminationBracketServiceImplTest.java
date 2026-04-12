package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.model.EliminationBracket;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.Match;
import com.escuela.techcup.core.model.Tournament;
import com.escuela.techcup.core.service.StandingsService;
import com.escuela.techcup.core.service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EliminationBracketServiceImplTest {

    @Mock
    private StandingsService standingsService;
    @Mock
    private MatchService matchService;
    @InjectMocks
    private EliminationBracketServiceImpl bracketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Team buildTeam(String id, String name, String tournamentId) {
        Team t = new Team(id, name, "color", null, null);
        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);
        t.setTournament(tournament);
        return t;
    }

    private List<Team> buildTeams(int n, String tournamentId) {
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            teams.add(buildTeam("id" + i, "Equipo" + i, tournamentId));
        }
        return teams;
    }

    private Match buildMatch(String id, Team teamA, Team teamB, String status) {
        return new Match(id, null, teamA, teamB, null, null, null, status);
    }

    private List<Match> buildMatches(int n, String tournamentId, String status) {
        List<Match> matches = new ArrayList<>();
        Team teamA = buildTeam("idA", "A", tournamentId);
        Team teamB = buildTeam("idB", "B", tournamentId);
        for (int i = 0; i < n; i++) {
            matches.add(buildMatch("m" + i, teamA, teamB, status));
        }
        return matches;
    }

    @Nested
    class BracketGeneration {

        @Test
        void noBracketsIfMatchesNotFinished() {
            String tournamentId = "T1";
            when(matchService.getAllMatches()).thenReturn(buildMatches(8, tournamentId, "PENDING"));
            when(standingsService.getStandingsTable(tournamentId)).thenReturn(buildTeams(8, tournamentId));
            EliminationBracket bracket = bracketService.getBracketsForTournament(tournamentId);
            assertNull(bracket);
        }

        @Test
        void bracketsFor8Teams() {
            String tournamentId = "T2";
            when(matchService.getAllMatches()).thenReturn(buildMatches(10, tournamentId, "FINISHED"));
            when(standingsService.getStandingsTable(tournamentId)).thenReturn(buildTeams(8, tournamentId));
            EliminationBracket bracket = bracketService.getBracketsForTournament(tournamentId);
            assertNotNull(bracket);
            assertNotNull(bracket.getQuarterFinals());
            assertEquals(4, bracket.getQuarterFinals().size());
        }

        @Test
        void bracketsFor16Teams() {
            String tournamentId = "T3";
            when(matchService.getAllMatches()).thenReturn(buildMatches(20, tournamentId, "FINISHED"));
            when(standingsService.getStandingsTable(tournamentId)).thenReturn(buildTeams(16, tournamentId));
            EliminationBracket bracket = bracketService.getBracketsForTournament(tournamentId);
            assertNotNull(bracket);
            assertNotNull(bracket.getRoundOf16());
            assertEquals(8, bracket.getRoundOf16().size());
        }

        @Test
        void bracketsFor4Teams() {
            String tournamentId = "T4";
            when(matchService.getAllMatches()).thenReturn(buildMatches(6, tournamentId, "FINISHED"));
            when(standingsService.getStandingsTable(tournamentId)).thenReturn(buildTeams(4, tournamentId));
            EliminationBracket bracket = bracketService.getBracketsForTournament(tournamentId);
            assertNotNull(bracket);
            assertNotNull(bracket.getSemiFinals());
            assertEquals(2, bracket.getSemiFinals().size());
        }

        @Test
        void bracketsFor2Teams() {
            String tournamentId = "T5";
            when(matchService.getAllMatches()).thenReturn(buildMatches(1, tournamentId, "FINISHED"));
            when(standingsService.getStandingsTable(tournamentId)).thenReturn(buildTeams(2, tournamentId));
            EliminationBracket bracket = bracketService.getBracketsForTournament(tournamentId);
            assertNotNull(bracket);
            assertNotNull(bracket.getFinals());
            assertEquals(1, bracket.getFinals().size());
        }
    }
}
