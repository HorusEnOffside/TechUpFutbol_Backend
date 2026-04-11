package com.escuela.techcup.persistence.mapper.tournament;

import com.escuela.techcup.core.model.Tournament;
import com.escuela.techcup.core.model.enums.TournamentStatus;
import com.escuela.techcup.persistence.entity.tournament.TournamentEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TournamentMapperTest {

    // ── toModel ──────────────────────────────────────────────────────────

    @Test
    void toModel_returnsNullWhenEntityIsNull() {
        assertNull(TournamentMapper.toModel(null));
    }

    @Test
    void toModel_mapsAllFieldsCorrectly() {
        TournamentEntity entity = new TournamentEntity();
        entity.setId("tour-1");
        entity.setStartDate(LocalDateTime.of(2025, 6, 1, 8, 0));
        entity.setEndDate(LocalDateTime.of(2025, 7, 1, 8, 0));
        entity.setClosingDate(LocalDateTime.of(2025, 5, 20, 8, 0));
        entity.setTeamsMaxAmount(8);
        entity.setTeamCost(50.0);
        entity.setStatus(TournamentStatus.ACTIVE);
        entity.setReglamento("Reglamento oficial");
        entity.setCanchas("Cancha A, Cancha B");
        entity.setHorarios("08:00-18:00");
        entity.setSanciones("Tarjeta roja = 1 partido");

        Tournament model = TournamentMapper.toModel(entity);

        assertNotNull(model);
        assertEquals("tour-1", model.getId());
        assertEquals(LocalDateTime.of(2025, 6, 1, 8, 0), model.getStartDate());
        assertEquals(LocalDateTime.of(2025, 7, 1, 8, 0), model.getEndDate());
        assertEquals(LocalDateTime.of(2025, 5, 20, 8, 0), model.getClosingDate());
        assertEquals(8, model.getTeamsMaxAmount());
        assertEquals(50.0, model.getTeamCost());
        assertEquals(TournamentStatus.ACTIVE, model.getStatus());
        assertEquals("Reglamento oficial", model.getReglamento());
        assertEquals("Cancha A, Cancha B", model.getCanchas());
        assertEquals("08:00-18:00", model.getHorarios());
        assertEquals("Tarjeta roja = 1 partido", model.getSanciones());
    }

    @Test
    void toModel_withNullOptionalFields_mapsCorrectly() {
        TournamentEntity entity = new TournamentEntity();
        entity.setId("tour-2");
        entity.setStartDate(LocalDateTime.of(2025, 6, 1, 8, 0));
        entity.setEndDate(LocalDateTime.of(2025, 7, 1, 8, 0));
        entity.setTeamsMaxAmount(4);
        entity.setTeamCost(0.0);
        entity.setStatus(TournamentStatus.DRAFT);

        Tournament model = TournamentMapper.toModel(entity);

        assertNotNull(model);
        assertNull(model.getClosingDate());
        assertNull(model.getReglamento());
        assertNull(model.getCanchas());
        assertNull(model.getHorarios());
        assertNull(model.getSanciones());
    }

    // ── toEntity ─────────────────────────────────────────────────────────

    @Test
    void toEntity_returnsNullWhenModelIsNull() {
        assertNull(TournamentMapper.toEntity(null));
    }

    @Test
    void toEntity_mapsAllFieldsCorrectly() {
        Tournament model = new Tournament();
        model.setId("tour-3");
        model.setStartDate(LocalDateTime.of(2025, 8, 1, 9, 0));
        model.setEndDate(LocalDateTime.of(2025, 9, 1, 9, 0));
        model.setClosingDate(LocalDateTime.of(2025, 7, 15, 9, 0));
        model.setTeamsMaxAmount(16);
        model.setTeamCost(100.0);
        model.setStatus(TournamentStatus.ACTIVE);
        model.setReglamento("Reglas");
        model.setCanchas("Cancha C");
        model.setHorarios("10:00-20:00");
        model.setSanciones("Ninguna");

        TournamentEntity entity = TournamentMapper.toEntity(model);

        assertNotNull(entity);
        assertEquals("tour-3", entity.getId());
        assertEquals(LocalDateTime.of(2025, 8, 1, 9, 0), entity.getStartDate());
        assertEquals(LocalDateTime.of(2025, 9, 1, 9, 0), entity.getEndDate());
        assertEquals(LocalDateTime.of(2025, 7, 15, 9, 0), entity.getClosingDate());
        assertEquals(16, entity.getTeamsMaxAmount());
        assertEquals(100.0, entity.getTeamCost());
        assertEquals(TournamentStatus.ACTIVE, entity.getStatus());
        assertEquals("Reglas", entity.getReglamento());
        assertEquals("Cancha C", entity.getCanchas());
        assertEquals("10:00-20:00", entity.getHorarios());
        assertEquals("Ninguna", entity.getSanciones());
    }

    @Test
    void toEntity_withNullOptionalFields_mapsCorrectly() {
        Tournament model = new Tournament();
        model.setId("tour-4");
        model.setStartDate(LocalDateTime.of(2025, 10, 1, 8, 0));
        model.setEndDate(LocalDateTime.of(2025, 11, 1, 8, 0));
        model.setTeamsMaxAmount(8);
        model.setTeamCost(25.0);
        model.setStatus(TournamentStatus.DRAFT);

        TournamentEntity entity = TournamentMapper.toEntity(model);

        assertNotNull(entity);
        assertNull(entity.getClosingDate());
        assertNull(entity.getReglamento());
        assertNull(entity.getCanchas());
        assertNull(entity.getHorarios());
        assertNull(entity.getSanciones());
    }

    // ── roundtrip ────────────────────────────────────────────────────────

    @Test
    void roundtrip_toEntityThenToModel_preservesData() {
        Tournament original = new Tournament();
        original.setId("tour-5");
        original.setStartDate(LocalDateTime.of(2025, 6, 1, 8, 0));
        original.setEndDate(LocalDateTime.of(2025, 7, 1, 8, 0));
        original.setTeamsMaxAmount(8);
        original.setTeamCost(50.0);
        original.setStatus(TournamentStatus.ACTIVE);
        original.setReglamento("Reglamento");
        original.setCanchas("Cancha A");
        original.setHorarios("08:00");
        original.setSanciones("Ninguna");

        Tournament result = TournamentMapper.toModel(TournamentMapper.toEntity(original));

        assertEquals(original.getId(), result.getId());
        assertEquals(original.getStatus(), result.getStatus());
        assertEquals(original.getReglamento(), result.getReglamento());
    }
}