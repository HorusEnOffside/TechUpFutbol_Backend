package com.escuela.techcup.core.model;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserPlayerTest {

    private UserPlayer userPlayer;

    @BeforeEach
    void setUp() {
        userPlayer = new UserPlayer("u1", "Ana", "ana@test.com",
                LocalDate.of(2000, 3, 10), Gender.MUJER, PlayerType.ESTUDIANTE, "pass456");
    }

    @Test
    void testGetId() {
        assertEquals("u1", userPlayer.getId());
    }

    @Test
    void testGetName() {
        assertEquals("Ana", userPlayer.getName());
    }

    @Test
    void testGetEmail() {
        assertEquals("ana@test.com", userPlayer.getEmail());
    }

    @Test
    void testGetGender() {
        assertEquals(Gender.MUJER, userPlayer.getGender());
    }

    @Test
    void testGetDateOfBirth() {
        assertEquals(LocalDate.of(2000, 3, 10), userPlayer.getDateOfBirth());
    }

    @Test
    void testGetPlayerType() {
        assertEquals(PlayerType.ESTUDIANTE, userPlayer.getPlayerType());
    }

    @Test
    void testGetPassword() {
        assertEquals("pass456", userPlayer.getPassword());
    }

    @Test
    void testProfilePictureEsNulaPorDefecto() {
        assertNull(userPlayer.getProfilePicture());
    }

    @Test
    void testSetPlayerType() {
        userPlayer.setPlayerType(PlayerType.GRADUADO);
        assertEquals(PlayerType.GRADUADO, userPlayer.getPlayerType());
    }

    @Test
    void testSetName() {
        userPlayer.setName("Laura");
        assertEquals("Laura", userPlayer.getName());
    }

    @Test
    void testSetEmail() {
        userPlayer.setEmail("laura@test.com");
        assertEquals("laura@test.com", userPlayer.getEmail());
    }

    @Test
    void testPlayerTypePROFESOR() {
        UserPlayer profesor = new UserPlayer("u2", "Luis", "luis@test.com",
                LocalDate.of(1985, 1, 1), Gender.HOMBRE, PlayerType.PROFESOR, "abc");
        assertEquals(PlayerType.PROFESOR, profesor.getPlayerType());
    }

    @Test
    void testPlayerTypeFAMILIAR() {
        UserPlayer familiar = new UserPlayer("u3", "Maria", "maria@test.com",
                LocalDate.of(1995, 6, 20), Gender.MUJER, PlayerType.FAMILIAR, "xyz");
        assertEquals(PlayerType.FAMILIAR, familiar.getPlayerType());
    }

    @Test
    void testConstructorConProfilePicture() {
        BufferedImage imagen = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Student s = new Student("s4", "Sofia", "sofia@test.com", imagen,
                LocalDate.of(2002, 5, 10), Gender.MUJER, 3, "pass");

        assertEquals("s4", s.getId());
        assertEquals("Sofia", s.getName());
        assertEquals("sofia@test.com", s.getEmail());
        assertEquals(3, s.getSemester());
        assertEquals(Gender.MUJER, s.getGender());
        assertEquals(PlayerType.ESTUDIANTE, s.getPlayerType());
        assertNotNull(s.getProfilePicture());
    }
}
