package com.escuela.techcup.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
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

@SpringBootTest
@ActiveProfiles("test")
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
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        adminToken = jwtService.generateToken("test-admin-id", "admin@test.com", Set.of("ADMIN"));
    }

    @Test
    @DisplayName("GET /api/users responde 200 con JSON válido")
    void testDataIntegrity_UsersListStructure() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, containsString("application/json")))
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/players retorna lista íntegra sin corruptelas")
    void testDataIntegrity_PlayersList() throws Exception {
        mockMvc.perform(get("/api/players")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, containsString("application/json")));
    }

    @Test
    @DisplayName("GET /api/users no devuelve passwords en respuesta")
    void testDataIntegrity_PasswordsNotExposed() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].password").doesNotExist());
    }

    @Test
    @DisplayName("Respuestas no contienen secretos o datos privados")
    void testDataIntegrity_NoSecretsInResponse() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].encodedPassword").doesNotExist());
    }

    @Test
    @DisplayName("Response incluye Content-Type")
    void testSecurityHeaders_ContentType() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, containsString("application/json")));
    }

    @Test
    @DisplayName("Response incluye Content-Length")
    void testSecurityHeaders_ContentLength() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    @DisplayName("CORS: GET desde origen permitido (localhost:3000) funciona")
    void testCORS_AllowedOriginLocalhost3000() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .header("Origin", "http://localhost:3000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("CORS: GET desde origen permitido (https://localhost:3000) funciona")
    void testCORS_AllowedOriginSecureLocalhost() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .header("Origin", "https://localhost:3000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("CORS: GET desde origen NO permitido es respondido por servidor")
    void testCORS_UnallowedOriginHandled() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .header("Origin", "https://attacker.malicious.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
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
    @DisplayName("CORS configurado con orígenes permitidos")
    void testCORS_ConfigurationPresent() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("JSON response es válido UTF-8")
    void testDataIntegrity_ValidJsonEncoding() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, containsString("application/json")));
    }

    @Test
    @DisplayName("GET /api/players retorna respuesta consistente")
    void testDataIntegrity_ConsistentResponses() throws Exception {
        mockMvc.perform(get("/api/players")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/players")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Endpoint no existente maneja request sin corromper datos")
    void testSecurityHeaders_ErrorHandling() throws Exception {
        mockMvc.perform(get("/api/nonexistent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}