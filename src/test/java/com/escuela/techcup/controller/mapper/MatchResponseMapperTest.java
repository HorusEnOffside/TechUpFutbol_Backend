package com.escuela.techcup.controller.mapper;

import com.escuela.techcup.controller.dto.MatchResponseDTO;
import com.escuela.techcup.core.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchResponseMapperTest {

    @Test
    void toDTO_returnsNullWhenMatchIsNull() {
        assertNull(MatchResponseMapper.toDTO(null));
    }

    @Test
    void toDTO_mapsBasicFields() {
        Match match = new Match("m-1", LocalDateTime.of(2026, 4, 17, 10, 0), null, null);

        MatchResponseDTO dto = MatchResponseMapper.toDTO(match);

        assertNotNull(dto);
        assertEquals("m-1", dto.getId());
        assertEquals("PENDING", dto.getStatus());
        assertNull(dto.getTeamA());
        assertNull(dto.getTeamB());
        assertNull(dto.getReferee());
        assertNull(dto.getSoccerField());
        assertTrue(dto.getGoals().isEmpty());
    }

    @Test
    void toDTO_mapsTeams() {
        Team teamA = new Team("t1", "Los Tigres", "rojo", null, null);
        Team teamB = new Team("t2", "Los Leones", "azul", null, null);
        Match match = new Match("m-1", LocalDateTime.now(), teamA, teamB);

        MatchResponseDTO dto = MatchResponseMapper.toDTO(match);

        assertEquals("t1", dto.getTeamA().getId());
        assertEquals("Los Tigres", dto.getTeamA().getName());
        assertEquals("t2", dto.getTeamB().getId());
        assertEquals("Los Leones", dto.getTeamB().getName());
    }

    @Test
    void toDTO_mapsSoccerFieldWithFoto() {
        SoccerField field = new SoccerField("sf-1", "Cancha 1", "Bloque A");
        field.setFoto(new byte[]{1, 2, 3});
        Team teamA = new Team("t1", "A", "rojo", null, null);
        Team teamB = new Team("t2", "B", "azul", null, null);
        Match match = new Match("m-1", LocalDateTime.now(), teamA, teamB,
                null, field, List.of(), "PENDING");

        MatchResponseDTO dto = MatchResponseMapper.toDTO(match);

        assertNotNull(dto.getSoccerField());
        assertEquals("Cancha 1", dto.getSoccerField().getName());
        assertNotNull(dto.getSoccerField().getFoto());
    }

    @Test
    void toDTO_mapsSoccerFieldWithoutFoto() {
        SoccerField field = new SoccerField("sf-1", "Cancha 2", "Bloque B");
        Team teamA = new Team("t1", "A", "rojo", null, null);
        Team teamB = new Team("t2", "B", "azul", null, null);
        Match match = new Match("m-1", LocalDateTime.now(), teamA, teamB,
                null, field, List.of(), "PENDING");

        MatchResponseDTO dto = MatchResponseMapper.toDTO(match);

        assertNull(dto.getSoccerField().getFoto());
    }
}