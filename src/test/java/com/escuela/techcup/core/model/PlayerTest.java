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
                LocalDate.of(2001, 4, 12), Gender.HOMBRE, "pass");
        team = new Team();
        player = new Player(userPlayer, Position.DELANTERO, 9);
    }

    @Test
    void testConstructorSinEquipo_userPlayer() {
        assertEquals(userPlayer, player.getUserPlayer());
    }

    @Test
    void testConstructorSinEquipo_position() {
        assertEquals(Position.DELANTERO, player.getPosition());
    }

    @Test
    void testConstructorSinEquipo_dorsalNumber() {
        assertEquals(9, player.getDorsalNumber());
    }

    @Test
    void testConstructorSinEquipo_statusEsDisponible() {
        assertEquals(PlayerStatus.DISPONIBLE, player.getStatus());
    }

    @Test
    void testConstructorSinEquipo_equipoEsNulo() {
        assertNull(player.getTeam());
    }

    @Test
    void testConstructorConEquipo_userPlayer() {
        Player p = new Player(userPlayer, Position.PORTERO, 1, team);
        assertEquals(userPlayer, p.getUserPlayer());
    }

    @Test
    void testConstructorConEquipo_position() {
        Player p = new Player(userPlayer, Position.PORTERO, 1, team);
        assertEquals(Position.PORTERO, p.getPosition());
    }

    @Test
    void testConstructorConEquipo_dorsalNumber() {
        Player p = new Player(userPlayer, Position.PORTERO, 1, team);
        assertEquals(1, p.getDorsalNumber());
    }

    @Test
    void testConstructorConEquipo_statusEsEnEquipo() {
        Player p = new Player(userPlayer, Position.PORTERO, 1, team);
        assertEquals(PlayerStatus.EN_EQUIPO, p.getStatus());
    }

    @Test
    void testConstructorConEquipo_equipoAsignado() {
        Player p = new Player(userPlayer, Position.PORTERO, 1, team);
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
        assertEquals(Gender.HOMBRE, player.getGender());
    }

    @Test
    void testGetPassword() {
        assertEquals("pass", player.getPassword());
    }

    @Test
    void testGetProfilePictureEsNuloPorDefecto() {
        assertNull(player.getProfilePicture());
    }

    @Test
    void testGetProfilePictureConImagen() {
        BufferedImage imagen = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        UserPlayer upConFoto = new UserPlayer("u2", "Ana", "ana@test.com", imagen,
                LocalDate.of(2000, 1, 1), Gender.MUJER, "pass");
        Player p = new Player(upConFoto, Position.VOLANTE, 7);
        assertNotNull(p.getProfilePicture());
    }

    @Test
    void testSetPosition() {
        player.setPosition(Position.VOLANTE);
        assertEquals(Position.VOLANTE, player.getPosition());
    }

    @Test
    void testSetDorsalNumber() {
        player.setDorsalNumber(10);
        assertEquals(10, player.getDorsalNumber());
    }

    @Test
    void testSetStatus() {
        player.setStatus(PlayerStatus.EN_EQUIPO);
        assertEquals(PlayerStatus.EN_EQUIPO, player.getStatus());
    }

    @Test
    void testSetTeam() {
        player.setTeam(team);
        assertEquals(team, player.getTeam());
    }

    @Test
    void testPosicionVolante() {
        Player p = new Player(userPlayer, Position.VOLANTE, 8);
        assertEquals(Position.VOLANTE, p.getPosition());
    }

    @Test
    void testPosicionDefensa() {
        Player p = new Player(userPlayer, Position.DEFENSA, 5);
        assertEquals(Position.DEFENSA, p.getPosition());
    }

    @Test
    void testPosicionPortero() {
        Player p = new Player(userPlayer, Position.PORTERO, 1);
        assertEquals(Position.PORTERO, p.getPosition());
    }

    @Test
    void testEquals_mismoObjeto() {
        assertEquals(player, player);
    }

    @Test
    void testEquals_objetosIguales() {
        Player p2 = new Player(userPlayer, Position.DELANTERO, 9);
        assertEquals(player, p2);
    }

    @Test
    void testEquals_objetosDiferentes() {
        Player p2 = new Player(userPlayer, Position.PORTERO, 1);
        assertNotEquals(player, p2);
    }

    @Test
    void testEquals_conNulo() {
        assertNotEquals(player, null);
    }

    @Test
    void testEquals_distintaClase() {
        assertNotEquals(player, "un string cualquiera");
    }

    @Test
    void testHashCode_objetosIguales() {
        Player p2 = new Player(userPlayer, Position.DELANTERO, 9);
        assertEquals(player.hashCode(), p2.hashCode());
    }

    @Test
    void testHashCode_objetosDiferentes() {
        Player p2 = new Player(userPlayer, Position.PORTERO, 1);
        assertNotEquals(player.hashCode(), p2.hashCode());
    }

    @Test
    void testToString_noEsNulo() {
        assertNotNull(player.toString());
    }

    @Test
    void testToString_contienePosition() {
        assertTrue(player.toString().contains("DELANTERO"));
    }

    @Test
    void testToString_contieneDorsalNumber() {
        assertTrue(player.toString().contains("9"));
    }
}
