package com.escuela.techcup.security.service;

import com.escuela.techcup.core.exception.TechcupException;
import com.escuela.techcup.core.model.Administrator;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.core.service.AuthenticationService;
import com.escuela.techcup.core.service.JwtService;
import com.escuela.techcup.core.service.UserService;
import com.escuela.techcup.controller.dto.LoginRequest;
import com.escuela.techcup.controller.dto.LoginResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private Administrator userMock;

    @BeforeEach
    void setUp() {
        userMock = new Administrator("u-1", "Admin", "admin@test.com",
                LocalDate.of(1990, 1, 1), Gender.MALE, "hashedPass");
        userMock.setPrimaryRole(UserRole.ADMIN);
    }

    @Test
    void login_returnsTokenWhenCredentialsAreValid() {
        when(userService.getUserByMail("admin@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("Password1", "hashedPass")).thenReturn(true);
        when(jwtService.generateToken(any(), any(), any())).thenReturn("token.jwt");

        LoginRequest request = new LoginRequest("admin@test.com", "Password1");
        LoginResponse response = authenticationService.login(request);

        assertNotNull(response);
        assertEquals("token.jwt", response.getToken());
    }

    @Test
    void login_returnsCorrectEmail() {
        when(userService.getUserByMail("admin@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("Password1", "hashedPass")).thenReturn(true);
        when(jwtService.generateToken(any(), any(), any())).thenReturn("token.jwt");

        LoginRequest request = new LoginRequest("admin@test.com", "Password1");
        LoginResponse response = authenticationService.login(request);

        assertEquals("admin@test.com", response.getEmail());
    }

    @Test
    void login_throwsWhenUserNotFound() {
        when(userService.getUserByMail("noexiste@test.com")).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest("noexiste@test.com", "Password1");

        assertThrows(TechcupException.class, () -> authenticationService.login(request));
    }

    @Test
    void login_throwsWhenPasswordIsWrong() {
        when(userService.getUserByMail("admin@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("WrongPass", "hashedPass")).thenReturn(false);

        LoginRequest request = new LoginRequest("admin@test.com", "WrongPass");

        assertThrows(TechcupException.class, () -> authenticationService.login(request));
    }

    @Test
    void login_callsJwtServiceWhenCredentialsAreValid() {
        when(userService.getUserByMail("admin@test.com")).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches("Password1", "hashedPass")).thenReturn(true);
        when(jwtService.generateToken(any(), any(), any())).thenReturn("token.jwt");

        authenticationService.login(new LoginRequest("admin@test.com", "Password1"));

        verify(jwtService, times(1)).generateToken(any(), any(), any());
    }

    //@Test
    //void login_doesNotCallJwtServiceWhenUserNotFound() {
        //when(userService.getUserByMail("noexiste@test.com")).thenReturn(Optional.empty());

        //assertThrows(TechcupException.class,
                //() -> authenticationService.login(new LoginRequest("noexiste@test.com", "Password1")));

        //verify(jwtService, never()).generateToken(any(), any(), any());
    //}

    @Test
    void login_doesNotCallJwtServiceWhenUserNotFound() {
        String email = "noexiste@test.com";
        LoginRequest loginRequest = new LoginRequest(email, "Password123");

        when(userService.getUserByMail(email)).thenReturn(Optional.empty());
        assertThrows(TechcupException.class,
                () -> authenticationService.login(loginRequest));
        verify(jwtService, never()).generateToken(any(), any(), any());
    }
}