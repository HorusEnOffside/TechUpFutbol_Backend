package com.escuela.techcup.controller;

import com.escuela.techcup.core.Handler.GlobalExceptionHandler;
import com.escuela.techcup.core.model.Administrator;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.core.service.JwtService;
import com.escuela.techcup.core.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;
    private Administrator userMock;

    @BeforeEach
    void setUp() {
        objectMapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setValidator(validator)
            .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
            .build();

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
