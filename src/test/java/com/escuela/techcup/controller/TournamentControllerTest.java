package com.escuela.techcup.controller;

import com.escuela.techcup.controller.handler.GlobalExceptionHandler;
import com.escuela.techcup.core.exception.TournamentNotFoundException;
import com.escuela.techcup.core.model.Tournament;
import com.escuela.techcup.core.model.enums.TournamentStatus;
import com.escuela.techcup.core.service.TournamentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.escuela.techcup.controller.dto.CanchaDTO;
import com.escuela.techcup.controller.dto.HorarioDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TournamentControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock private TournamentService tournamentService;
    @InjectMocks private TournamentController tournamentController;

    private Tournament tournament;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tournamentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        tournament = new Tournament();
        tournament.setId("tour-1");
        tournament.setStartDate(LocalDateTime.of(2026, 6, 1, 0, 0));
        tournament.setEndDate(LocalDateTime.of(2026, 7, 1, 0, 0));
        tournament.setTeamsMaxAmount(8);
        tournament.setTeamCost(50.0);
        tournament.setStatus(TournamentStatus.ACTIVE);
    }

    // --- GET /api/tournaments ---

    @Test
    void getAllTournaments_returns200() throws Exception {
        when(tournamentService.getAllTournaments()).thenReturn(List.of(tournament));

        mockMvc.perform(get("/api/tournaments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("tour-1"));
    }

    @Test
    void getAllTournaments_returnsEmptyList() throws Exception {
        when(tournamentService.getAllTournaments()).thenReturn(List.of());

        mockMvc.perform(get("/api/tournaments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // --- GET /api/tournaments/{id} ---

    @Test
    void getTournamentById_returns200WhenFound() throws Exception {
        when(tournamentService.getTournamentById("tour-1")).thenReturn(tournament);

        mockMvc.perform(get("/api/tournaments/tour-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("tour-1"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void getTournamentById_returns404WhenNotFound() throws Exception {
        when(tournamentService.getTournamentById("no-existe"))
                .thenThrow(new TournamentNotFoundException("no-existe"));

        mockMvc.perform(get("/api/tournaments/no-existe"))
                .andExpect(status().isNotFound());
    }

    // --- GET /api/tournaments/active ---

    @Test
    void getActiveTournament_returns200WhenExists() throws Exception {
        when(tournamentService.getActiveTournament()).thenReturn(tournament);

        mockMvc.perform(get("/api/tournaments/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    // --- POST /api/tournaments ---

    @Test
    void createTournament_returns201WhenValid() throws Exception {
        when(tournamentService.createTournament(any(), any(), eq(8), eq(50.0), any(), any()))
                .thenReturn(tournament);

        String body = objectMapper.writeValueAsString(new com.escuela.techcup.controller.dto.CreateTournamentDTO() {{
            setStartDate(LocalDateTime.of(2026, 6, 1, 0, 0));
            setEndDate(LocalDateTime.of(2026, 7, 1, 0, 0));
            setTeamsMaxAmount(8);
            setTeamCost(50.0);
            setStatus(TournamentStatus.DRAFT);
            setOrganizerId("org-1");
        }});

        mockMvc.perform(post("/api/tournaments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    // --- PUT /api/tournaments/{id}/finalize ---

    @Test
    void finalizeTournament_returns200() throws Exception {
        mockMvc.perform(put("/api/tournaments/tour-1/finalize"))
                .andExpect(status().isOk());
    }

    // --- PUT /api/tournaments/{id}/configure ---

    @Test
    void configureTournament_returns200WhenValid() throws Exception {
        when(tournamentService.configureTournament(any(), any(), any(), any()))
                .thenReturn(tournament);

        com.escuela.techcup.controller.dto.ConfigureTournamentDTO dto =
                new com.escuela.techcup.controller.dto.ConfigureTournamentDTO();
        dto.setReglamento("Reglamento de prueba");
        dto.setClosingDate(LocalDateTime.of(2026, 5, 1, 0, 0));

        String body = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/tournaments/tour-1/configure")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }
}