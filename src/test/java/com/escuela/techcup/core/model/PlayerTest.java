package com.escuela.techcup.core.model;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private UserPlayer userPlayer;
    private Team team;

    @BeforeEach
    void setUp() {
        userPlayer = new UserPlayer("u1", "Pedro", "pedro@test.com",
                LocalDate.of(2001, 4, 12), Gender.HOMBRE, "pass");
        team = new Team();
    }

    // --- Constructor sin equipo ---

    @Test
    void testConstructorSinEquipo_userPlayer() {
        Player player = new Player(userPlayer, Position.DELANTERO, 9);
        assertEquals(userPlayer, player.getUserPlayer());
    }

    @Test
    void testConstructorSinEquipo_position() {
        Player player = new Player(userPlayer, Position.DELANTERO, 9);
        assertEquals(Position.DELANTERO, player.getPosition());
    }

    @Test
    void testConstructorSinEquipo_dorsalNumber() {
        Player player = new Player(userPlayer, Position.DELANTERO, 9);
        assertEquals(9, player.getDorsalNumber());
    }

    @Test
    void testConstructorSinEquipo_statusEsDisponible() {
        Player player = new Player(userPlayer, Position.DELANTERO, 9);
        assertEquals(PlayerStatus.DISPONIBLE, player.getStatus());
    }

    @Test
    void testConstructorSinEquipo_equipoEsNulo() {
        Player player = new Player(userPlayer, Position.DELANTERO, 9);
        assertNull(player.getTeam());
    }

    // --- Constructor con equipo ---

    @Test
    void testConstructorConEquipo_userPlayer() {
        Player player = new Player(userPlayer, Position.PORTERO, 1, team);
        assertEquals(userPlayer, player.getUserPlayer());
    }

    @Test
    void testConstructorConEquipo_position() {
        Player player = new Player(userPlayer, Position.PORTERO, 1, team);
        assertEquals(Position.PORTERO, player.getPosition());
    }

    @Test
    void testConstructorConEquipo_dorsalNumber() {
        Player player = new Player(userPlayer, Position.PORTERO, 1, team);
        assertEquals(1, player.getDorsalNumber());
    }

    @Test
    void testConstructorConEquipo_statusEsEnEquipo() {
        Player player = new Player(userPlayer, Position.PORTERO, 1, team);
        assertEquals(PlayerStatus.EN_EQUIPO, player.getStatus());
    }

    @Test
    void testConstructorConEquipo_equipoAsignado() {
        Player player = new Player(userPlayer, Position.PORTERO, 1, team);
        assertEquals(team, player.getTeam());
    }

    // --- Setters ---

    @Test
    void testSetPosition() {
        Player player = new Player(userPlayer, Position.DEFENSA, 4);
        player.setPosition(Position.VOLANTE);
        assertEquals(Position.VOLANTE, player.getPosition());
    }

    @Test
    void testSetDorsalNumber() {
        Player player = new Player(userPlayer, Position.DEFENSA, 4);
        player.setDorsalNumber(10);
        assertEquals(10, player.getDorsalNumber());
    }

    @Test
    void testSetStatus() {
        Player player = new Player(userPlayer, Position.DEFENSA, 4);
        player.setStatus(PlayerStatus.EN_EQUIPO);
        assertEquals(PlayerStatus.EN_EQUIPO, player.getStatus());
    }

    // --- Posiciones ---

    @Test
    void testPosicionVolante() {
        Player player = new Player(userPlayer, Position.VOLANTE, 8);
        assertEquals(Position.VOLANTE, player.getPosition());
    }

    @Test
    void testPosicionDefensa() {
        Player player = new Player(userPlayer, Position.DEFENSA, 5);
        assertEquals(Position.DEFENSA, player.getPosition());
    }
    @Test
    void testGetUserId() {
        Player player = new Player(userPlayer, Position.DELANTERO, 9);
        assertEquals("u1", player.getUserId());
    }

    @Test
    void testGetName() {
        Player player = new Player(userPlayer, Position.DELANTERO, 9);
        assertEquals("Pedro", player.getName());
    }

    @Test
    void testGetMail() {
        Player player = new Player(userPlayer, Position.DELANTERO, 9);
        assertEquals("pedro@test.com", player.getMail());
    }

    @Test
    void testGetDateOfBirth() {
        Player player = new Player(userPlayer, Position.DELANTERO, 9);
        assertEquals(LocalDate.of(2001, 4, 12), player.getDateOfBirth());
    }

    @Test
    void testGetGender() {
        Player player = new Player(userPlayer, Position.DELANTERO, 9);
        assertEquals(Gender.HOMBRE, player.getGender());
    }

    @Test
    void testGetPassword() {
        Player player = new Player(userPlayer, Position.DELANTERO, 9);
        assertEquals("pass", player.getPassword());
    }
}
