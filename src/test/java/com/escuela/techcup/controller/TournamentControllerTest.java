package com.escuela.techcup.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TournamentControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TournamentController tournamentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tournamentController).build();
    }

    @Test
    void testHealth_returns200() throws Exception {
        mockMvc.perform(get("/api/tournaments/health"))
            .andExpect(status().isOk());
    }

    @Test
    void testHealth_returnsExpectedMessage() throws Exception {
        mockMvc.perform(get("/api/tournaments/health"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
            .andExpect(content().string("Tournament controller OK"));
    }
}
