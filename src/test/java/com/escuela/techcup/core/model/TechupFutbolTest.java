package com.escuela.techcup.core.model;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TechupFutbolTest {

    private TechupFutbol techupFutbol;

    @BeforeEach
    void setUp() {
        techupFutbol = new TechupFutbol();
    }


    @Test
    void testTorneosNulosPorDefecto() {
        assertNull(techupFutbol.getTournaments());
    }

    @Test
    void testUsuariosNulosPorDefecto() {
        assertNull(techupFutbol.getUsers());
    }

    @Test
    void testJugadoresNulosPorDefecto() {
        assertNull(techupFutbol.getPlayers());
    }

    @Test
    void testSetTournaments() {
        List<Tournament> torneos = List.of(new Tournament());
        techupFutbol.setTournaments(torneos);
        assertEquals(1, techupFutbol.getTournaments().size());
    }

    @Test
    void testSetUsers() {
        Organizer organizer = new Organizer("1", "Carlos", "carlos@test.com",
                LocalDate.of(1990, 1, 1), Gender.MALE, "pass");
        techupFutbol.setUsers(List.of(organizer));
        assertEquals(1, techupFutbol.getUsers().size());
    }

    @Test
    void testSetPlayers() {
        UserPlayer up = new UserPlayer("u1", "Ana", "ana@test.com",
                LocalDate.of(2000, 1, 1), Gender.FEMALE, "pass");
        Player player = new Player(up, Position.FORWARD, 9);
        techupFutbol.setPlayers(List.of(player));
        assertEquals(1, techupFutbol.getPlayers().size());
    }

    @Test
    void testSetTournamentsVacia() {
        techupFutbol.setTournaments(List.of());
        assertTrue(techupFutbol.getTournaments().isEmpty());
    }

    @Test
    void testSetUsersVacia() {
        techupFutbol.setUsers(List.of());
        assertTrue(techupFutbol.getUsers().isEmpty());
    }

    @Test
    void testSetPlayersVacia() {
        techupFutbol.setPlayers(List.of());
        assertTrue(techupFutbol.getPlayers().isEmpty());
    }

    @Test
    void testSetMultiplesTorneos() {
        List<Tournament> torneos = List.of(new Tournament(), new Tournament(), new Tournament());
        techupFutbol.setTournaments(torneos);
        assertEquals(3, techupFutbol.getTournaments().size());
    }

    @Test
    void testSetMultiplesJugadores() {
        UserPlayer up1 = new UserPlayer("u1", "Pedro", "pedro@test.com",
                LocalDate.of(2001, 4, 12), Gender.MALE, "pass");
        UserPlayer up2 = new UserPlayer("u2", "Maria", "maria@test.com",
                LocalDate.of(2002, 5, 15), Gender.FEMALE, "pass");
        techupFutbol.setPlayers(List.of(
                new Player(up1, Position.GOALKEEPER, 1),
                new Player(up2, Position.DEFENDER, 4)
        ));
        assertEquals(2, techupFutbol.getPlayers().size());
    }
}
