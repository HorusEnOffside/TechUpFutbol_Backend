package com.escuela.techcup.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.escuela.techcup.core.model.Match;
import com.escuela.techcup.core.service.MatchService;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)

class MatchControllerTest {

    private MockMvc mockMvc;


    @Mock
    private MatchService matchService;

    @InjectMocks
    private MatchController matchController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(matchController).build();
    }

    @Test
    void getMatchById_returnsMatch() throws Exception {
        Match match = mock(Match.class);
        when(matchService.getMatchById("1")).thenReturn(match);
        mockMvc.perform(get("/api/matches/1"))
            .andExpect(status().isOk());
        verify(matchService).getMatchById("1");
    }

    @Test
    void getAllMatches_returnsList() throws Exception {
        when(matchService.getAllMatches()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/matches"))
            .andExpect(status().isOk());
        verify(matchService).getAllMatches();
    }



    @Test
    void createMatch_returnsCreated() throws Exception {
        Match match = mock(Match.class);
        when(matchService.createMatch(any(), anyString(), anyString())).thenReturn(match);
        mockMvc.perform(post("/api/matches")
                .param("date", "2024-01-01")
                .param("teamAId", "A")
                .param("teamBId", "B"))
            .andExpect(status().isCreated());
        verify(matchService).createMatch(any(), eq("A"), eq("B"));
    }

    @Test
    void setReferee_returnsOk() throws Exception {
        Match match = mock(Match.class);
        when(matchService.setReferee(anyString(), anyString())).thenReturn(match);
        mockMvc.perform(put("/api/matches/1/referee")
                .param("refereeId", "ref1"))
            .andExpect(status().isOk());
        verify(matchService).setReferee("1", "ref1");
    }

    @Test
    void setSoccerField_returnsOk() throws Exception {
        Match match = mock(Match.class);
        when(matchService.setSoccerField(anyString(), anyString())).thenReturn(match);
        mockMvc.perform(post("/api/matches/1/soccer-field")
                .param("soccerFieldId", "sf1"))
            .andExpect(status().isOk());
        verify(matchService).setSoccerField("1", "sf1");
    }

    @Test
    void addMatchEventGoal_returnsOk() throws Exception {
        Match match = mock(Match.class);
        when(matchService.addMatchEventGoal(anyString(), anyString(), anyInt(), anyString())).thenReturn(match);
        mockMvc.perform(post("/api/matches/1/events/goal")
                .param("playerId", "p1")
                .param("minute", "10")
                .param("description", "desc"))
            .andExpect(status().isOk());
        verify(matchService).addMatchEventGoal("1", "p1", 10, "desc");
    }

    @Test
    void getMyMatches_returnsMatchesForReferee() throws Exception {
        Match match = mock(Match.class);
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("referee-uuid");
        when(matchService.getMatchesByRefereeId("referee-uuid")).thenReturn(List.of(match));

        mockMvc.perform(get("/api/matches/my-matches")
                .principal(auth))
            .andExpect(status().isOk());

        verify(matchService).getMatchesByRefereeId("referee-uuid");
    }

    @Test
    void getMyMatches_returnsEmptyWhenNoMatches() throws Exception {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("referee-no-matches");
        when(matchService.getMatchesByRefereeId("referee-no-matches")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/matches/my-matches")
                .principal(auth))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));

        verify(matchService).getMatchesByRefereeId("referee-no-matches");
    }

}