package com.escuela.techcup.security.service;

import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;

    private static final String SECRET = "test-secret-key-for-jwt-signing-must-be-long-enough-for-security-purposes-change-this-in-production-xd";
    private static final long EXPIRATION = 86400000L;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION);
    }

    @Test
    void generateToken_returnsNonNullToken() {
        String token = jwtService.generateToken("user-1", "user@test.com", Set.of("ADMIN"));
        assertNotNull(token);
    }

    @Test
    void generateToken_returnsDifferentTokensForDifferentUsers() {
        String token1 = jwtService.generateToken("user-1", "user1@test.com", Set.of("ADMIN"));
        String token2 = jwtService.generateToken("user-2", "user2@test.com", Set.of("ADMIN"));
        assertNotEquals(token1, token2);
    }

    @Test
    void generateToken_throwsWhenUserIdIsNull() {
        Set<String> roles = Set.of("ADMIN");
        assertThrows(InvalidInputException.class,
                () -> jwtService.generateToken(null, "user@test.com", roles));
    }

    @Test
    void generateToken_throwsWhenUserIdIsBlank() {
        Set<String> roles = Set.of("ADMIN");
        assertThrows(InvalidInputException.class,
                () -> jwtService.generateToken("  ", "user@test.com", roles));
    }

    @Test
    void generateToken_throwsWhenEmailIsNull() {
        Set<String> roles = Set.of("ADMIN");
        assertThrows(InvalidInputException.class,
                () -> jwtService.generateToken("user-1", null, roles));
    }

    @Test
    void generateToken_throwsWhenEmailIsBlank() {
        Set<String> roles = Set.of("ADMIN");
        assertThrows(InvalidInputException.class,
                () -> jwtService.generateToken("user-1", " ", roles)
        );
    }

    @Test
    void generateToken_throwsWhenRolesIsNull() {
        assertThrows(InvalidInputException.class,
                () -> jwtService.generateToken("user-1", "user@test.com", null));
    }

    @Test
    void generateToken_throwsWhenRolesIsEmpty() {
        Set<String> emptyRoles = Set.of();

        assertThrows(InvalidInputException.class,
                () -> jwtService.generateToken("user-1", "user@test.com", emptyRoles)
        );
    }

    @Test
    void extractUserId_returnsCorrectUserId() {
        String token = jwtService.generateToken("user-1", "user@test.com", Set.of("ADMIN"));
        assertEquals("user-1", jwtService.extractUserId(token));
    }

    @Test
    void extractEmail_returnsCorrectEmail() {
        String token = jwtService.generateToken("user-1", "user@test.com", Set.of("ADMIN"));
        assertEquals("user@test.com", jwtService.extractEmail(token));
    }

    @Test
    void isTokenValid_returnsTrueForValidToken() {
        String token = jwtService.generateToken("user-1", "user@test.com", Set.of("ADMIN"));
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void isTokenValid_returnsFalseForNullToken() {
        assertFalse(jwtService.isTokenValid(null));
    }

    @Test
    void isTokenValid_returnsFalseForBlankToken() {
        assertFalse(jwtService.isTokenValid("  "));
    }

    @Test
    void isTokenValid_returnsFalseForInvalidToken() {
        assertFalse(jwtService.isTokenValid("token.invalido.aqui"));
    }
}