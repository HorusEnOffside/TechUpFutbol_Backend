package com.escuela.techcup.core.model;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private UserPlayer userPlayer;
    private Team team;
    private Player player;

    @BeforeEach
    void setUp() {
        userPlayer = new UserPlayer("u1", "Pedro", "pedro@test.com",
                LocalDate.of(2001, 4, 12), Gender.MALE, "pass");
        team = new Team();
        player = new Player(userPlayer, Position.FORWARD, 9);
    }

    @Test
    void testConstructorWithoutTeam_userPlayer() {
        assertEquals(userPlayer, player.getUserPlayer());
    }

    @Test
    void testConstructorWithoutTeam_position() {
        assertEquals(Position.FORWARD, player.getPosition());
    }

    @Test
    void testConstructorWithoutTeam_dorsalNumber() {
        assertEquals(9, player.getDorsalNumber());
    }

    @Test
    void testConstructorWithoutTeam_statusIsAvailable() {
        assertEquals(PlayerStatus.AVAILABLE, player.getStatus());
    }

    @Test
    void testConstructorWithoutTeam_teamIsNull() {
        assertNull(player.getTeam());
    }

    @Test
    void testConstructorWithTeam_userPlayer() {
        Player p = new Player(userPlayer, Position.GOALKEEPER, 1, team);
        assertEquals(userPlayer, p.getUserPlayer());
    }

    @Test
    void testConstructorWithTeam_position() {
        Player p = new Player(userPlayer, Position.GOALKEEPER, 1, team);
        assertEquals(Position.GOALKEEPER, p.getPosition());
    }

    @Test
    void testConstructorWithTeam_dorsalNumber() {
        Player p = new Player(userPlayer, Position.GOALKEEPER, 1, team);
        assertEquals(1, p.getDorsalNumber());
    }

    @Test
    void testConstructorWithTeam_statusIsInTeam() {
        Player p = new Player(userPlayer, Position.GOALKEEPER, 1, team);
        assertEquals(PlayerStatus.IN_TEAM, p.getStatus());
    }

    @Test
    void testConstructorWithTeam_teamAssigned() {
        Player p = new Player(userPlayer, Position.GOALKEEPER, 1, team);
        assertEquals(team, p.getTeam());
    }

    @Test
    void testGetUserId() {
        assertEquals("u1", player.getUserId());
    }

    @Test
    void testGetName() {
        assertEquals("Pedro", player.getName());
    }

    @Test
    void testGetMail() {
        assertEquals("pedro@test.com", player.getMail());
    }

    @Test
    void testGetDateOfBirth() {
        assertEquals(LocalDate.of(2001, 4, 12), player.getDateOfBirth());
    }

    @Test
    void testGetGender() {
        assertEquals(Gender.MALE, player.getGender());
    }

    @Test
    void testGetPassword() {
        assertEquals("pass", player.getPassword());
    }

    @Test
    void testGetProfilePictureIsNullByDefault() {
        assertNull(player.getProfilePicture());
    }

    @Test
    void testGetProfilePictureWithImage() {
        BufferedImage imagen = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        UserPlayer upConFoto = new UserPlayer("u2", "Ana", "ana@test.com", imagen,
                LocalDate.of(2000, 1, 1), Gender.FEMALE, "pass");
        Player p = new Player(upConFoto, Position.MIDFIELDER, 7);
        assertNotNull(p.getProfilePicture());
    }

    @Test
    void testSetPosition() {
        player.setPosition(Position.MIDFIELDER);
        assertEquals(Position.MIDFIELDER, player.getPosition());
    }

    @Test
    void testSetDorsalNumber() {
        player.setDorsalNumber(10);
        assertEquals(10, player.getDorsalNumber());
    }

    @Test
    void testSetStatus() {
        player.setStatus(PlayerStatus.IN_TEAM);
        assertEquals(PlayerStatus.IN_TEAM, player.getStatus());
    }

    @Test
    void testSetTeam() {
        player.setTeam(team);
        assertEquals(team, player.getTeam());
    }

    @Test
    void testMidfielderPosition() {
        Player p = new Player(userPlayer, Position.MIDFIELDER, 8);
        assertEquals(Position.MIDFIELDER, p.getPosition());
    }

    @Test
    void testDefenderPosition() {
        Player p = new Player(userPlayer, Position.DEFENDER, 5);
        assertEquals(Position.DEFENDER, p.getPosition());
    }

    @Test
    void testGoalkeeperPosition() {
        Player p = new Player(userPlayer, Position.GOALKEEPER, 1);
        assertEquals(Position.GOALKEEPER, p.getPosition());
    }

    @Test
    void testEquals_sameObject() {
        assertEquals(player, player);
    }

    @Test
    void testEquals_equalObjects() {
        Player p2 = new Player(userPlayer, Position.FORWARD, 9);
        assertEquals(player, p2);
    }

    @Test
    void testEquals_differentObjects() {
        Player p2 = new Player(userPlayer, Position.GOALKEEPER, 1);
        assertNotEquals(player, p2);
    }

    @Test
    void testEquals_withNull() {
        assertNotEquals(null, player);
    }

    @Test
    void testEquals_differentClass() {
        assertNotEquals("un string cualquiera", player);
    }

    @Test
    void testHashCode_equalObjects() {
        Player p2 = new Player(userPlayer, Position.FORWARD, 9);
        assertEquals(player.hashCode(), p2.hashCode());
    }

    @Test
    void testHashCode_differentObjects() {
        Player p2 = new Player(userPlayer, Position.GOALKEEPER, 1);
        assertNotEquals(player.hashCode(), p2.hashCode());
    }

    @Test
    void testToString_notNull() {
        assertNotNull(player.toString());
    }

    @Test
    void testToString_containsPosition() {
        assertTrue(player.toString().contains("FORWARD"));
    }

    @Test
    void testToString_containsDorsalNumber() {
        assertTrue(player.toString().contains("9"));
    }
}

