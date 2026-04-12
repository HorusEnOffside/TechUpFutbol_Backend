package com.escuela.techcup.controller;

import com.escuela.techcup.core.model.EliminationBracket;
import com.escuela.techcup.core.service.EliminationBracketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EliminationBracketControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EliminationBracketService bracketService;

    @InjectMocks
    private EliminationBracketController bracketController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bracketController).build();
    }

    @Test
    void getBrackets_returnsBrackets() throws Exception {
        EliminationBracket bracket = new EliminationBracket();
        when(bracketService.getBracketsForTournament("T1")).thenReturn(bracket);

        mockMvc.perform(get("/api/brackets/T1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eliminationBrackets").exists())
                .andExpect(jsonPath("$.message").value("Llaves generadas correctamente"));
    }

    @Test
    void getBrackets_returnsMessageIfNoBrackets() throws Exception {
        when(bracketService.getBracketsForTournament("T2")).thenReturn(null);

        mockMvc.perform(get("/api/brackets/T2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eliminationBrackets").doesNotExist())
                .andExpect(jsonPath("$.message").value("No se pueden generar llaves: hay partidos pendientes o no hay suficientes equipos."));
    }
}
