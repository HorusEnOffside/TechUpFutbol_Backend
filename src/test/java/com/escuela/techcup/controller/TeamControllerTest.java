package com.escuela.techcup.controller;

import com.escuela.techcup.controller.handler.GlobalExceptionHandler;
import com.escuela.techcup.core.exception.TeamNotFoundException;
import com.escuela.techcup.core.model.Invitation;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.enums.Formation;
import com.escuela.techcup.core.model.enums.InvitationStatus;
import com.escuela.techcup.core.service.TeamFullInfoService;
import com.escuela.techcup.core.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TeamControllerTest {

    private MockMvc mockMvc;

    @Mock private TeamService teamService;
    @Mock private TeamFullInfoService teamFullInfoService;

    @InjectMocks private TeamController teamController;

    private Team team;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(teamController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        team = new Team("team-1", "Los Tigres", "Rojo", null, Formation.FORMATION_4_4_2);
    }

    // --- GET /api/teams ---

    @Test
    void getAllTeams_returns200() throws Exception {
        when(teamService.getAllTeams()).thenReturn(List.of(team));

        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Los Tigres"));
    }

    @Test
    void getAllTeams_returnsEmptyList() throws Exception {
        when(teamService.getAllTeams()).thenReturn(List.of());

        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // --- GET /api/teams/{teamId} ---

    @Test
    void getTeamById_returns200WhenFound() throws Exception {
        when(teamService.getTeamById("team-1")).thenReturn(team);

        mockMvc.perform(get("/api/teams/team-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Los Tigres"));
    }

    @Test
    void getTeamById_returns404WhenNotFound() throws Exception {
        when(teamService.getTeamById("no-existe"))
                .thenThrow(new TeamNotFoundException("no-existe"));

        mockMvc.perform(get("/api/teams/no-existe"))
                .andExpect(status().isNotFound());
    }

    // --- POST /api/teams/{teamId}/invite/{playerId} ---

    @Test
    void invitePlayer_returns200WhenValid() throws Exception {
        doNothing().when(teamService).invitePlayer(eq("team-1"), eq("player-1"), any());

        mockMvc.perform(post("/api/teams/team-1/invite/player-1")
                        .param("message", "Únete al equipo"))
                .andExpect(status().isOk());
    }

    // --- PUT /api/teams/invitations/{invitationId} ---

    @Test
    void respondInvitation_returns200WhenAccepted() throws Exception {
        doNothing().when(teamService).respondInvitation("inv-1", InvitationStatus.ACCEPTED);

        mockMvc.perform(put("/api/teams/invitations/inv-1")
                        .param("action", "ACCEPTED"))
                .andExpect(status().isOk());
    }

    @Test
    void respondInvitation_returns200WhenRejected() throws Exception {
        doNothing().when(teamService).respondInvitation("inv-1", InvitationStatus.REJECTED);

        mockMvc.perform(put("/api/teams/invitations/inv-1")
                        .param("action", "REJECTED"))
                .andExpect(status().isOk());
    }

    // --- GET /api/teams/invitations/player/{playerId} ---

    @Test
    void getInvitationsByPlayer_returns200WithList() throws Exception {
        Invitation inv = new Invitation();
        inv.setId("inv-1");
        inv.setTeamId("team-1");
        inv.setTeamName("Los Tigres");
        inv.setMessage("Únete");
        inv.setStatus(InvitationStatus.PENDING);

        when(teamService.getInvitationsByPlayer("player-1")).thenReturn(List.of(inv));

        mockMvc.perform(get("/api/teams/invitations/player/player-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("inv-1"))
                .andExpect(jsonPath("$[0].teamName").value("Los Tigres"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void getInvitationsByPlayer_returns200WithEmptyList() throws Exception {
        when(teamService.getInvitationsByPlayer("player-1")).thenReturn(List.of());

        mockMvc.perform(get("/api/teams/invitations/player/player-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // --- GET /api/teams/{teamId}/validate/composition ---

    @Test
    void validateTeamComposition_returns200True() throws Exception {
        when(teamService.validateTeamComposition("team-1")).thenReturn(true);

        mockMvc.perform(get("/api/teams/team-1/validate/composition"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void validateTeamComposition_returns200False() throws Exception {
        when(teamService.validateTeamComposition("team-1")).thenReturn(false);

        mockMvc.perform(get("/api/teams/team-1/validate/composition"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    // --- GET /api/teams/{teamId}/validate/engineering ---

    @Test
    void validateEngineeringMajority_returns200() throws Exception {
        when(teamService.validateEngineeringMajority("team-1")).thenReturn(true);

        mockMvc.perform(get("/api/teams/team-1/validate/engineering"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    // --- GET /api/teams/validate/player/{playerId}/tournament/{tournamentId} ---

    @Test
    void validatePlayerUniquePerTournament_returns200() throws Exception {
        when(teamService.validatePlayerUniquePerTournament("p1", "t1")).thenReturn(true);

        mockMvc.perform(get("/api/teams/validate/player/p1/tournament/t1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    // --- GET /api/teams/search?name= ---

    @Nested
    class SearchTeamByName {

        @Test
        void retorna200ConIdYNombreCuandoEncuentra() throws Exception {
            when(teamService.findByNameContaining("Tigres"))
                    .thenReturn(Optional.of(team));

            mockMvc.perform(get("/api/teams/search").param("name", "Tigres"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("team-1"))
                    .andExpect(jsonPath("$.name").value("Los Tigres"));
        }

        @Test
        void retorna404CuandoNoEncuentra() throws Exception {
            when(teamService.findByNameContaining("xyz"))
                    .thenReturn(Optional.empty());

            mockMvc.perform(get("/api/teams/search").param("name", "xyz"))
                    .andExpect(status().isNotFound());
        }
    }
}