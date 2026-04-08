package com.escuela.techcup.controller;

import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.MatchEvent;
import com.escuela.techcup.core.service.StandingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StandingsControllerTest {
    private MockMvc mockMvc;

    @Mock
    private StandingsService standingsService;

    @InjectMocks
    private StandingsController standingsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(standingsController).build();
    }

    @Test
    void getStandingsTable_returnsTeams() throws Exception {
        Team team = mock(Team.class);
        when(standingsService.getStandingsTable("1")).thenReturn(List.of(team));
        mockMvc.perform(get("/api/standings/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void getTopScorers_returnsPlayers() throws Exception {
        Player player = mock(Player.class);
        when(standingsService.getTopScorers("1")).thenReturn(List.of(player));
        mockMvc.perform(get("/api/standings/1/top-scorers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void getCardsHistory_returnsMatchEvents() throws Exception {
        // Crear un UserPlayer real
        com.escuela.techcup.core.model.UserPlayer userPlayer = new com.escuela.techcup.core.model.UserPlayer(
                "u1", "Nombre", "mail@mail.com", java.time.LocalDate.of(2000,1,1),
                com.escuela.techcup.core.model.enums.Gender.MALE, "pass");
        // Crear un Player real
        com.escuela.techcup.core.model.Player player = new com.escuela.techcup.core.model.Player(
                userPlayer, com.escuela.techcup.core.model.enums.Position.DEFENDER, 5);
        // Crear un Card real
        com.escuela.techcup.core.model.Card card = new com.escuela.techcup.core.model.Card(
                "c1", 10, player, com.escuela.techcup.core.model.Card.CardType.YELLOW, "Falta");
        when(standingsService.getCardsHistory("1", null)).thenReturn(List.of(card));
        mockMvc.perform(get("/api/standings/1/cards-history").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void getCardsHistory_returnsEmptyListWhenNoEvents() throws Exception {
        when(standingsService.getCardsHistory("1", null)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/standings/1/cards-history").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getCardsHistory_withPlayerOrTeamId_returnsMatchEvents() throws Exception {
        // Crear un UserPlayer real
        com.escuela.techcup.core.model.UserPlayer userPlayer = new com.escuela.techcup.core.model.UserPlayer(
                "u2", "Nombre2", "mail2@mail.com", java.time.LocalDate.of(2001,2,2),
                com.escuela.techcup.core.model.enums.Gender.FEMALE, "pass2");
        // Crear un Player real
        com.escuela.techcup.core.model.Player player = new com.escuela.techcup.core.model.Player(
                userPlayer, com.escuela.techcup.core.model.enums.Position.FORWARD, 9);
        // Crear un Card real
        com.escuela.techcup.core.model.Card card = new com.escuela.techcup.core.model.Card(
                "c2", 20, player, com.escuela.techcup.core.model.Card.CardType.RED, "Mano");
        when(standingsService.getCardsHistory("1", "2")).thenReturn(List.of(card));
        mockMvc.perform(get("/api/standings/1/cards-history?playerOrTeamId=2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }
}
