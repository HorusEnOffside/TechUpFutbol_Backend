package com.escuela.techcup.security;

import com.escuela.techcup.controller.AdminController;
import com.escuela.techcup.controller.TournamentController;
import com.escuela.techcup.controller.handler.GlobalExceptionHandler;
import com.escuela.techcup.controller.MatchController;
import com.escuela.techcup.controller.PaymentController;
import com.escuela.techcup.core.service.AdminService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthorizationIntegrationTest {

    private MockMvc tournamentMvc;
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
        MockMvc adminMvc = MockMvcBuilders.standaloneSetup(adminController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        tournamentMvc = MockMvcBuilders.standaloneSetup(tournamentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        MockMvc matchMvc = MockMvcBuilders.standaloneSetup(matchController)
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

}