package com.escuela.techcup.controller;

import com.escuela.techcup.config.SecurityConfig;
import com.escuela.techcup.core.exception.TechcupException;
import com.escuela.techcup.core.model.Administrator;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.core.service.JwtService;
import com.escuela.techcup.core.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper;
    private Administrator userMock;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        userMock = new Administrator("1", "Carlos", "carlos@test.com",
                LocalDate.of(1990, 5, 15), Gender.HOMBRE, "encodedPass");
        userMock.setPrimaryRole(UserRole.ADMIN);
    }

    private String loginJson(String email, String password) {
        return String.format("""
                {
                    "email": %s,
                    "password": %s
                }
                """,
                email == null ? "null" : "\"" + email + "\"",
                password == null ? "null" : "\"" + password + "\"");
    }

    @Test
    void testLogin_retorna200() throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("pass123", "encodedPass")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString(), any())).thenReturn("token.jwt.test");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "pass123")))
                .andExpect(status().isOk());
    }

    @Test
    void testLogin_retornaToken() throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("pass123", "encodedPass")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString(), any())).thenReturn("token.jwt.test");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "pass123")))
                .andExpect(jsonPath("$.token").value("token.jwt.test"));
    }

    @Test
    void testLogin_retornaEmail() throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("pass123", "encodedPass")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString(), any())).thenReturn("token.jwt.test");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "pass123")))
                .andExpect(jsonPath("$.email").value("carlos@test.com"));
    }

    @Test
    void testLogin_retornaUserId() throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("pass123", "encodedPass")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString(), any())).thenReturn("token.jwt.test");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "pass123")))
                .andExpect(jsonPath("$.userId").value("1"));
    }

    @Test
    void testLogin_retornaRoles() throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("pass123", "encodedPass")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString(), any())).thenReturn("token.jwt.test");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "pass123")))
                .andExpect(jsonPath("$.roles").isArray());
    }

    @Test
    void testLogin_llamaAlServicioDeUsuario() throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString(), any())).thenReturn("token.jwt.test");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "pass123")));

        verify(userService, times(1)).getUserByMail("carlos@test.com");
    }

    @Test
    void testLogin_llamaAlJwtService() throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString(), any())).thenReturn("token.jwt.test");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "pass123")));

        verify(jwtService, times(1)).generateToken(anyString(), anyString(), any());
    }

    @Test
    void testLogin_usuarioNoExiste_retorna401() throws Exception {
        when(userService.getUserByMail("noexiste@test.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("noexiste@test.com", "pass123")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogin_passwordIncorrecta_retorna401() throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "wrongPass")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogin_sinEmail_retorna400() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson(null, "pass123")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_emailInvalido_retorna400() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("no-es-email", "pass123")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_sinPassword_retorna400() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_noLlamaJwtSiPasswordIncorrecta() throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "wrongPass")));

        verify(jwtService, never()).generateToken(anyString(), anyString(), any());
    }

    @Test
    void testLogin_noLlamaJwtSiUsuarioNoExiste() throws Exception {
        when(userService.getUserByMail("noexiste@test.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("noexiste@test.com", "pass123")));

        verify(jwtService, never()).generateToken(anyString(), anyString(), any());
    }
}
