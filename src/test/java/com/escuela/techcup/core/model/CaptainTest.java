package com.escuela.techcup.core.model;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CaptainTest {

    private Player player;
    private Captain captain;

    @BeforeEach
    void setUp() {
        UserPlayer userPlayer = new UserPlayer("u1", "Lucas", "lucas@test.com",
                LocalDate.of(1999, 7, 3), Gender.MALE, "pass");
        player = new Player(userPlayer, Position.MIDFIELDER, 10);
        captain = new Captain(player);
    }

    @Test
    void testCaptainNoEsNulo() {
        assertNotNull(captain);
    }

    @Test
    void testGetPosition() {
        assertEquals(Position.MIDFIELDER, captain.getPosition());
    }

    @Test
    void testGetDorsalNumber() {
        assertEquals(10, captain.getDorsalNumber());
    }

    @Test
    void testGetStatus() {
        assertEquals(PlayerStatus.AVAILABLE, captain.getStatus());
    }

    @Test
    void testGetUserPlayer() {
        assertNotNull(captain.getUserPlayer());
        assertEquals("Lucas", captain.getUserPlayer().getName());
    }

    @Test
    void testGetComponentPlayer() {
        assertEquals(player, captain.getComponentPlayer());
    }

    @Test
    void testGetUserPlayerId() {
        assertEquals("u1", captain.getUserPlayer().getId());
    }

    @Test
    void testGetUserPlayerEmail() {
        assertEquals("lucas@test.com", captain.getUserPlayer().getMail());
    }

    @Test
    void testDecoratorDelegaPosition() {
        player.setPosition(Position.DEFENDER);
        assertEquals(Position.DEFENDER, captain.getPosition());
    }

    @Test
    void testDecoratorDelegaStatus() {
        player.setStatus(PlayerStatus.IN_TEAM);
        assertEquals(PlayerStatus.IN_TEAM, captain.getStatus());
    }

    @Test
    void testDecoratorDelegaDorsalNumber() {
        player.setDorsalNumber(5);
        assertEquals(5, captain.getDorsalNumber());
    }
}
