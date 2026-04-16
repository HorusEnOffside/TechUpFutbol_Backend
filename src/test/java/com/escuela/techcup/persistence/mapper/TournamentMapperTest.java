package com.escuela.techcup.persistence.mapper.tournament;

import com.escuela.techcup.core.model.Cancha;
import com.escuela.techcup.core.model.Horario;
import com.escuela.techcup.core.model.Tournament;
import com.escuela.techcup.core.model.enums.TournamentStatus;
import com.escuela.techcup.persistence.entity.tournament.CanchaEntity;
import com.escuela.techcup.persistence.entity.tournament.HorarioEntity;
import com.escuela.techcup.persistence.entity.tournament.TournamentEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
        entity.setSanciones("Tarjeta roja = 1 partido");

        CanchaEntity canchaEntity = new CanchaEntity();
        canchaEntity.setId("c-1");
        canchaEntity.setNombre("Cancha A");
        entity.getCanchas().add(canchaEntity);

        HorarioEntity horarioEntity = new HorarioEntity();
        horarioEntity.setId("h-1");
        horarioEntity.setFecha(LocalDate.of(2025, 6, 10));
        horarioEntity.setDescripcion("Jornada 1");
        entity.getHorarios().add(horarioEntity);

        Tournament model = TournamentMapper.toModel(entity);

        assertNotNull(model);
        assertEquals("tour-1", model.getId());
        assertEquals(TournamentStatus.ACTIVE, model.getStatus());
        assertEquals("Reglamento oficial", model.getReglamento());
        assertEquals("Tarjeta roja = 1 partido", model.getSanciones());
        assertEquals(1, model.getCanchas().size());
        assertEquals("Cancha A", model.getCanchas().get(0).getNombre());
        assertEquals(1, model.getHorarios().size());
        assertEquals("Jornada 1", model.getHorarios().get(0).getDescripcion());
    }

    @Test
    void toModel_withEmptyLists_mapsCorrectly() {
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
        assertNull(model.getSanciones());
        assertTrue(model.getCanchas().isEmpty());
        assertTrue(model.getHorarios().isEmpty());
    }

    // ── toEntity ─────────────────────────────────────────────────────────

    @Test
    void toEntity_returnsNullWhenModelIsNull() {
        assertNull(TournamentMapper.toEntity(null));
    }

    @Test
    void toEntity_mapsScalarFieldsCorrectly() {
        Tournament model = new Tournament();
        model.setId("tour-3");
        model.setStartDate(LocalDateTime.of(2025, 8, 1, 9, 0));
        model.setEndDate(LocalDateTime.of(2025, 9, 1, 9, 0));
        model.setClosingDate(LocalDateTime.of(2025, 7, 15, 9, 0));
        model.setTeamsMaxAmount(16);
        model.setTeamCost(100.0);
        model.setStatus(TournamentStatus.ACTIVE);
        model.setReglamento("Reglas");
        model.setSanciones("Ninguna");

        Cancha cancha = new Cancha();
        cancha.setId("c-1");
        cancha.setNombre("Cancha C");
        model.setCanchas(List.of(cancha));

        Horario horario = new Horario();
        horario.setId("h-1");
        horario.setFecha(LocalDate.of(2025, 8, 10));
        horario.setDescripcion("Jornada 1");
        model.setHorarios(List.of(horario));

        TournamentEntity entity = TournamentMapper.toEntity(model);

        assertNotNull(entity);
        assertEquals("tour-3", entity.getId());
        assertEquals(TournamentStatus.ACTIVE, entity.getStatus());
        assertEquals("Reglas", entity.getReglamento());
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
        assertNull(entity.getSanciones());
    }

    // ── roundtrip ────────────────────────────────────────────────────────

    @Test
    void roundtrip_toEntityThenToModel_preservesScalarData() {
        Tournament original = new Tournament();
        original.setId("tour-5");
        original.setStartDate(LocalDateTime.of(2025, 6, 1, 8, 0));
        original.setEndDate(LocalDateTime.of(2025, 7, 1, 8, 0));
        original.setTeamsMaxAmount(8);
        original.setTeamCost(50.0);
        original.setStatus(TournamentStatus.ACTIVE);
        original.setReglamento("Reglamento");
        original.setSanciones("Ninguna");
        original.setCanchas(List.of());
        original.setHorarios(List.of());

        Tournament result = TournamentMapper.toModel(TournamentMapper.toEntity(original));

        assertEquals(original.getId(), result.getId());
        assertEquals(original.getStatus(), result.getStatus());
        assertEquals(original.getReglamento(), result.getReglamento());
    }
}
