package com.escuela.techcup.security;

import com.escuela.techcup.controller.AdminController;
import com.escuela.techcup.controller.TournamentController;
import com.escuela.techcup.controller.MatchController;
import com.escuela.techcup.controller.PaymentController;
import com.escuela.techcup.core.service.AdminService;
import com.escuela.techcup.handler.GlobalExceptionHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthorizationIntegrationTest {

    private MockMvc adminMvc;
    private MockMvc tournamentMvc;
    private MockMvc matchMvc;
    private MockMvc paymentMvc;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @InjectMocks
    private TournamentController tournamentController;

    @InjectMocks
    private MatchController matchController;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        adminMvc = MockMvcBuilders.standaloneSetup(adminController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        tournamentMvc = MockMvcBuilders.standaloneSetup(tournamentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        matchMvc = MockMvcBuilders.standaloneSetup(matchController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        paymentMvc = MockMvcBuilders.standaloneSetup(paymentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // --- Tournament endpoints con rol ORGANIZER ---

    @Test
    void tournamentHealth_withOrganizerRole_returns200() throws Exception {
        tournamentMvc.perform(get("/api/tournaments/health")
                        .with(user("org").roles("ORGANIZER")))
                .andExpect(status().isOk());
    }

    // --- Match endpoints con rol ORGANIZER y REFEREE ---

    @Test
    void matchHealth_withOrganizerRole_returns200() throws Exception {
        matchMvc.perform(get("/api/matches/health")
                        .with(user("org").roles("ORGANIZER")))
                .andExpect(status().isOk());
    }

    @Test
    void matchHealth_withRefereeRole_returns200() throws Exception {
        matchMvc.perform(get("/api/matches/health")
                        .with(user("ref").roles("REFEREE")))
                .andExpect(status().isOk());
    }

    // --- Payment endpoints con rol ORGANIZER ---

    @Test
    void paymentHealth_withOrganizerRole_returns200() throws Exception {
        paymentMvc.perform(get("/api/payments/health")
                        .with(user("org").roles("ORGANIZER")))
                .andExpect(status().isOk());
    }
}