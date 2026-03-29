package com.escuela.techcup.controller;

import com.escuela.techcup.handler.GlobalExceptionHandler;
import com.escuela.techcup.core.model.Administrator;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.core.service.JwtService;
import com.escuela.techcup.core.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    private Administrator userMock;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setValidator(validator)
            .build();

        userMock = new Administrator("1", "Carlos", "carlos@test.com",
                LocalDate.of(1990, 5, 15), Gender.MALE, "encodedPass");
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
    void testLogin_returns200() throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("pass123", "encodedPass")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString(), any())).thenReturn("token.jwt.test");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "pass123")))
                .andExpect(status().isOk());
    }

            @ParameterizedTest
            @MethodSource("loginResponseFields")
            void testLogin_returnsExpectedField(String jsonPathExpression, String expectedValue) throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("pass123", "encodedPass")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString(), any())).thenReturn("token.jwt.test");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "pass123")))
                .andExpect(jsonPath(jsonPathExpression).value(expectedValue));
    }

            private static Stream<Arguments> loginResponseFields() {
            return Stream.of(
                Arguments.of("$.token", "token.jwt.test"),
                Arguments.of("$.email", "carlos@test.com"),
                Arguments.of("$.userId", "1")
            );
    }

    @Test
    void testLogin_returnsRoles() throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("pass123", "encodedPass")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString(), any())).thenReturn("token.jwt.test");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "pass123")))
                .andExpect(jsonPath("$.roles").isArray());
    }

    @Test
    void testLogin_callsUserService() throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString(), any())).thenReturn("token.jwt.test");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "pass123")));

        verify(userService, times(1)).getUserByMail("carlos@test.com");
    }

    @Test
    void testLogin_callsJwtService() throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString(), any())).thenReturn("token.jwt.test");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "pass123")));

        verify(jwtService, times(1)).generateToken(anyString(), anyString(), any());
    }

    @Test
    void testLogin_userDoesNotExist_returns401() throws Exception {
        when(userService.getUserByMail("noexiste@test.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("noexiste@test.com", "pass123")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogin_wrongPassword_returns401() throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "wrongPass")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogin_withoutEmail_returns400() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson(null, "pass123")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_invalidEmail_returns400() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("no-es-email", "pass123")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_withoutPassword_returns400() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_doesNotCallJwtIfWrongPassword() throws Exception {
        when(userService.getUserByMail("carlos@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("carlos@test.com", "wrongPass")));

        verify(jwtService, never()).generateToken(anyString(), anyString(), any());
    }

    @Test
    void testLogin_doesNotCallJwtIfUserDoesNotExist() throws Exception {
        when(userService.getUserByMail("noexiste@test.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson("noexiste@test.com", "pass123")));

        verify(jwtService, never()).generateToken(anyString(), anyString(), any());
    }
}
