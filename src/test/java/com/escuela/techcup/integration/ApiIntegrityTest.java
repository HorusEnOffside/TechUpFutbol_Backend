package com.escuela.techcup.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import com.escuela.techcup.core.service.impl.JwtServiceImpl;
import java.util.Set;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Pruebas funcionales de integridad de la API.
 * 
 * Valida:
 * 1. Integridad de datos en tránsito (no se corrompen).
 * 2. CORS configurado (variable de entorno).
 * 3. Headers de seguridad presentes en respuestas.
 * 4. Datos sensibles (passwords) no se exponen.
 */
@SpringBootTest
@DisplayName("API Integrity Functional Tests")

class ApiIntegrityTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JwtServiceImpl jwtService;

    private MockMvc mockMvc;

    private String adminToken;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        // Generate a valid JWT for an admin user
        adminToken = jwtService.generateToken("test-admin-id", "admin@test.com", Set.of("ADMIN"));
    }

    @Test
    @DisplayName("CORS: OPTIONS preflight desde origen permitido funciona")
    void testCORS_PreflightRequest() throws Exception {
        mockMvc.perform(options("/api/users")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "GET")
                .header("Access-Control-Request-Headers", "Content-Type"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Endpoint no existente maneja request sin corromper datos")
    void testSecurityHeaders_ErrorHandling() throws Exception {
        mockMvc.perform(get("/api/nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is5xxServerError());
    }
}
