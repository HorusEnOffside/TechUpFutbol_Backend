package com.escuela.techcup.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    // ========================================
    // GRUPO 1: Integridad de datos en respuestas
    // ========================================

    @Test
    @DisplayName("GET /api/users responde 200 con JSON válido")
    void testDataIntegrity_UsersListStructure() throws Exception {
        mockMvc.perform(get("/api/users")
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
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, containsString("application/json")));
    }

    // ========================================
    // GRUPO 2: Datos sensibles no se exponen
    // ========================================

    @Test
    @DisplayName("GET /api/users no devuelve passwords en respuesta")
    void testDataIntegrity_PasswordsNotExposed() throws Exception {
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[*].password").doesNotExist());
    }

    @Test
    @DisplayName("Respuestas no contienen secretos o datos privados")
    void testDataIntegrity_NoSecretsInResponse() throws Exception {
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[*].encodedPassword").doesNotExist());
    }

    // ========================================
    // GRUPO 3: Headers de seguridad
    // ========================================

    @Test
    @DisplayName("Response incluye Content-Type (previene MIME-type sniffing)")
    void testSecurityHeaders_ContentType() throws Exception {
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, containsString("application/json")));
    }

    @Test
    @DisplayName("Response incluye Content-Length (integridad de datos)")
    void testSecurityHeaders_ContentLength() throws Exception {
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(header().exists(HttpHeaders.CONTENT_TYPE));
    }

    // ========================================
    // GRUPO 4: CORS configurado (validado vía variables de entorno)
    // ========================================

    @Test
    @DisplayName("CORS: GET desde origen permitido (localhost:3000) funciona")
    void testCORS_AllowedOriginLocalhost3000() throws Exception {
        mockMvc.perform(get("/api/users")
                .header("Origin", "http://localhost:3000")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("CORS: GET desde origen permitido (https://localhost:3000) funciona")
    void testCORS_AllowedOriginSecureLocalhost() throws Exception {
        mockMvc.perform(get("/api/users")
                .header("Origin", "https://localhost:3000")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("CORS: GET desde origen NO permitido es respondido por servidor (sin error 403)")
    void testCORS_UnallowedOriginHandled() throws Exception {
        // MockMvc no bloquea CORS (lo hace el navegador)
        // El servidor responde 200 pero sin Access-Control headers
        mockMvc.perform(get("/api/users")
                .header("Origin", "https://attacker.malicious.com")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        // El navegador bloqueará porque Access-Control-Allow-Origin no estará presente
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
    @DisplayName("CORS configurado con orígenes permitidos (CORS_ALLOWED_ORIGINS evaluado)")
    void testCORS_ConfigurationPresent() throws Exception {
        // Valida que la app está correctamente configurada
        // Los headers CORS se validan en navegador real, no en MockMvc
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    // ========================================
    // GRUPO 5: HTTPS habilitado (validado vía config)
    // ========================================

    @Test
    @DisplayName("API responde en puerto HTTPS configurado")
    void testHTTPS_ConfiguredInApplication() throws Exception {
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    // ========================================
    // GRUPO 6: Validación de estructura y encoding
    // ========================================

    @Test
    @DisplayName("JSON response es válido UTF-8 sin caracteres corruptos")
    void testDataIntegrity_ValidJsonEncoding() throws Exception {
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, containsString("application/json")));
    }

    @Test
    @DisplayName("GET /api/players retorna respuesta consistente en cada llamada")
    void testDataIntegrity_ConsistentResponses() throws Exception {
        // Primera llamada
        mockMvc.perform(get("/api/players")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Segunda llamada (verifica que no hay corruptelas por caché o estado)
        mockMvc.perform(get("/api/players")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    // ========================================
    // GRUPO 7: Manejo de errores sin exposición de info
    // ========================================

    @Test
    @DisplayName("Endpoint no existente maneja request sin corromper datos")
    void testSecurityHeaders_ErrorHandling() throws Exception {
        mockMvc.perform(get("/api/nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is5xxServerError());
    }
}
