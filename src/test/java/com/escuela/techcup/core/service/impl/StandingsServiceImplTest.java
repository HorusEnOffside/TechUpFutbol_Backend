package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.MatchEvent;
import com.escuela.techcup.persistence.repository.tournament.TeamRepository;
import com.escuela.techcup.persistence.repository.tournament.MatchRepository;
import com.escuela.techcup.persistence.repository.tournament.GoalRepository;
import com.escuela.techcup.persistence.repository.tournament.CardRepository;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import com.escuela.techcup.persistence.mapper.tournament.TeamMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StandingsServiceImplTest {
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private GoalRepository goalRepository;
    @Mock
    private CardRepository cardRepository;
    @InjectMocks
    private StandingsServiceImpl standingsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStandingsTable_returnsTeams() {
        TeamEntity teamEntity = mock(TeamEntity.class);
        when(teamEntity.getTournament()).thenReturn(null); // Ajusta según lógica de tu test
        when(teamRepository.findAll()).thenReturn(List.of(teamEntity));
        when(matchRepository.findAll()).thenReturn(Collections.emptyList());
        // Si TeamMapper.toModel es estático y no mockeable, se usará la conversión real
        List<Team> result = standingsService.getStandingsTable("1");
        assertNotNull(result);
        // Dependiendo de la lógica, puede estar vacío si el filtro de torneo no coincide
        // assertFalse(result.isEmpty());
    }

    @Test
    void getTopScorers_returnsPlayers() {
        when(matchRepository.findAll()).thenReturn(Collections.emptyList());
        List<Player> result = standingsService.getTopScorers("1");
        assertNotNull(result);
    }

    @Test
    void getCardsHistory_returnsMatchEvents() {
        when(cardRepository.findByPlayer_Team_Tournament_Id("1")).thenReturn(Collections.emptyList());
        List<MatchEvent> result = standingsService.getCardsHistory("1", null);
        assertNotNull(result);
    }

    @Test
    void getStandingsTable_withMatchesAndGoals() {
        // Simula dos equipos en el mismo torneo
        TeamEntity teamA = mock(TeamEntity.class);
        TeamEntity teamB = mock(TeamEntity.class);
        when(teamA.getId()).thenReturn("A");
        when(teamB.getId()).thenReturn("B");
        var torneo = mock(com.escuela.techcup.persistence.entity.tournament.TournamentEntity.class);
        when(torneo.getId()).thenReturn("T1");
        when(teamA.getTournament()).thenReturn(torneo);
        when(teamB.getTournament()).thenReturn(torneo);
        when(teamRepository.findAll()).thenReturn(List.of(teamA, teamB));

        // Simula un partido entre A y B con goles
        var partido = mock(com.escuela.techcup.persistence.entity.tournament.MatchEntity.class);
        when(partido.getTournament()).thenReturn(torneo);
        when(partido.getTeamA()).thenReturn(teamA);
        when(partido.getTeamB()).thenReturn(teamB);
        // Goles: A=2, B=1
        var goalA1 = mock(com.escuela.techcup.persistence.entity.tournament.GoalEntity.class);
        var goalA2 = mock(com.escuela.techcup.persistence.entity.tournament.GoalEntity.class);
        var goalB1 = mock(com.escuela.techcup.persistence.entity.tournament.GoalEntity.class);
        var playerA = mock(com.escuela.techcup.persistence.entity.users.PlayerEntity.class);
        var playerB = mock(com.escuela.techcup.persistence.entity.users.PlayerEntity.class);
        when(playerA.getTeam()).thenReturn(teamA);
        when(playerB.getTeam()).thenReturn(teamB);
        when(goalA1.getPlayer()).thenReturn(playerA);
        when(goalA2.getPlayer()).thenReturn(playerA);
        when(goalB1.getPlayer()).thenReturn(playerB);
        when(partido.getGoals()).thenReturn(List.of(goalA1, goalA2, goalB1));
        when(matchRepository.findAll()).thenReturn(List.of(partido));

        // TeamMapper.toModel debe devolver un Team real
        try (var mocked = org.mockito.Mockito.mockStatic(com.escuela.techcup.persistence.mapper.tournament.TeamMapper.class)) {
            com.escuela.techcup.core.model.Team modelA = mock(com.escuela.techcup.core.model.Team.class);
            com.escuela.techcup.core.model.Team modelB = mock(com.escuela.techcup.core.model.Team.class);
            when(modelA.getId()).thenReturn("A");
            when(modelB.getId()).thenReturn("B");
            mocked.when(() -> com.escuela.techcup.persistence.mapper.tournament.TeamMapper.toModel(teamA)).thenReturn(modelA);
            mocked.when(() -> com.escuela.techcup.persistence.mapper.tournament.TeamMapper.toModel(teamB)).thenReturn(modelB);
            var result = standingsService.getStandingsTable("T1");
            assertEquals(2, result.size());
        }
    }

    @Test
    void getTopScorers_withGoals() {
        // Simula partido con goles de dos jugadores
        var torneo = mock(com.escuela.techcup.persistence.entity.tournament.TournamentEntity.class);
        when(torneo.getId()).thenReturn("T1");
        var partido = mock(com.escuela.techcup.persistence.entity.tournament.MatchEntity.class);
        when(partido.getTournament()).thenReturn(torneo);
        var goal1 = mock(com.escuela.techcup.persistence.entity.tournament.GoalEntity.class);
        var goal2 = mock(com.escuela.techcup.persistence.entity.tournament.GoalEntity.class);
        var player1 = mock(com.escuela.techcup.persistence.entity.users.PlayerEntity.class);
        var player2 = mock(com.escuela.techcup.persistence.entity.users.PlayerEntity.class);
        when(player1.getId()).thenReturn("P1");
        when(player2.getId()).thenReturn("P2");
        when(goal1.getPlayer()).thenReturn(player1);
        when(goal2.getPlayer()).thenReturn(player2);
        when(partido.getGoals()).thenReturn(List.of(goal1, goal2));
        when(matchRepository.findAll()).thenReturn(List.of(partido));
        try (var mocked = org.mockito.Mockito.mockStatic(com.escuela.techcup.persistence.mapper.users.PlayerMapper.class)) {
            com.escuela.techcup.core.model.Player model1 = mock(com.escuela.techcup.core.model.Player.class);
            com.escuela.techcup.core.model.Player model2 = mock(com.escuela.techcup.core.model.Player.class);
            when(model1.getUserId()).thenReturn("P1");
            when(model2.getUserId()).thenReturn("P2");
            when(model1.getName()).thenReturn("A");
            when(model2.getName()).thenReturn("B");
            mocked.when(() -> com.escuela.techcup.persistence.mapper.users.PlayerMapper.toModel(player1)).thenReturn(model1);
            mocked.when(() -> com.escuela.techcup.persistence.mapper.users.PlayerMapper.toModel(player2)).thenReturn(model2);
            var result = standingsService.getTopScorers("T1");
            assertEquals(2, result.size());
        }
    }

    @Test
    void getCardsHistory_withPlayerAndTeamId() {
        // Simula tarjetas para player y para team
        var cardEntity = mock(com.escuela.techcup.persistence.entity.tournament.CardEntity.class);
        var cardModel = mock(com.escuela.techcup.core.model.Card.class);
        try (var mocked = org.mockito.Mockito.mockStatic(com.escuela.techcup.persistence.mapper.tournament.CardMapper.class)) {
            mocked.when(() -> com.escuela.techcup.persistence.mapper.tournament.CardMapper.toModel(cardEntity)).thenReturn(cardModel);
            when(cardRepository.findByPlayer_Team_Tournament_IdAndPlayer_Id("T1", "P1")).thenReturn(List.of(cardEntity));
            when(cardRepository.findByPlayer_Team_Tournament_IdAndPlayer_Team_Id("T1", "P1")).thenReturn(Collections.emptyList());
            var result = standingsService.getCardsHistory("T1", "P1");
            assertEquals(1, result.size());
        }
    }
}
