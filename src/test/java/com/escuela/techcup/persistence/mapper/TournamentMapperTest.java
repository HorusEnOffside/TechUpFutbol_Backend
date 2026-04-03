package com.escuela.techcup.persistence.mapper.tournament;

import com.escuela.techcup.core.model.Tournament;
import com.escuela.techcup.core.model.enums.TournamentStatus;
import com.escuela.techcup.persistence.entity.tournament.TournamentEntity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TournamentMapperTest {

    private static final LocalDateTime START = LocalDateTime.of(2025, 1, 1, 0, 0);
    private static final LocalDateTime END   = LocalDateTime.of(2025, 6, 1, 0, 0);


    // -------------------------------------------------------------------------
    // toEntity
    // -------------------------------------------------------------------------

    @Nested
    class ToEntity {

        @Test
        void whenValidModel_thenMapsAllFields() {
            Tournament model = buildModel();

            TournamentEntity result = TournamentMapper.toEntity(model);

            assertNotNull(result);
            assertEquals("t-1", result.getId());
            assertEquals(START, result.getStartDate());
            assertEquals(END, result.getEndDate());
            assertEquals(8, result.getTeamsMaxAmount());
            assertEquals(150.0, result.getTeamCost());
            assertEquals(TournamentStatus.ACTIVE, result.getStatus());
        }

        @Test
        void whenStatusIsNull_thenEntityStatusIsNull() {
            Tournament model = buildModel();
            model.setStatus(null);

            TournamentEntity result = TournamentMapper.toEntity(model);

            assertNull(result.getStatus());
        }

        @Test
        void whenTeamCostIsZero_thenEntityTeamCostIsZero() {
            Tournament model = buildModel();
            model.setTeamCost(0.0);

            TournamentEntity result = TournamentMapper.toEntity(model);

            assertEquals(0.0, result.getTeamCost());
        }
    }


    // -------------------------------------------------------------------------
    // toModel
    // -------------------------------------------------------------------------

    @Nested
    class ToModel {

        @Test
        void whenValidEntity_thenMapsAllFields() {
            TournamentEntity entity = buildEntity();

            Tournament result = TournamentMapper.toModel(entity);

            assertNotNull(result);
            assertEquals("t-1", result.getId());
            assertEquals(START, result.getStartDate());
            assertEquals(END, result.getEndDate());
            assertEquals(8, result.getTeamsMaxAmount());
            assertEquals(150.0, result.getTeamCost());
            assertEquals(TournamentStatus.ACTIVE, result.getStatus());
        }

        @Test
        void whenStatusIsNull_thenModelStatusIsNull() {
            TournamentEntity entity = buildEntity();
            entity.setStatus(null);

            Tournament result = TournamentMapper.toModel(entity);

            assertNull(result.getStatus());
        }

        @Test
        void whenTeamCostIsNull_thenModelTeamCostIsNull() {
            TournamentEntity entity = buildEntity();
            entity.setTeamCost(null);

            Tournament result = TournamentMapper.toModel(entity);

            assertNull(result.getTeamCost());
        }
    }


    // -------------------------------------------------------------------------
    // roundtrip
    // -------------------------------------------------------------------------

    @Nested
    class Roundtrip {

        @Test
        void modelToEntityToModel_preservesAllFields() {
            Tournament original = buildModel();

            Tournament result = TournamentMapper.toModel(TournamentMapper.toEntity(original));

            assertEquals(original.getId(), result.getId());
            assertEquals(original.getStartDate(), result.getStartDate());
            assertEquals(original.getEndDate(), result.getEndDate());
            assertEquals(original.getTeamsMaxAmount(), result.getTeamsMaxAmount());
            assertEquals(original.getTeamCost(), result.getTeamCost());
            assertEquals(original.getStatus(), result.getStatus());
        }
    }


    // -------------------------------------------------------------------------
    // helpers
    // -------------------------------------------------------------------------

    private Tournament buildModel() {
        Tournament t = new Tournament();
        t.setId("t-1");
        t.setStartDate(START);
        t.setEndDate(END);
        t.setTeamsMaxAmount(8);
        t.setTeamCost(150.0);
        t.setStatus(TournamentStatus.ACTIVE);
        return t;
    }

    private TournamentEntity buildEntity() {
        TournamentEntity e = new TournamentEntity();
        e.setId("t-1");
        e.setStartDate(START);
        e.setEndDate(END);
        e.setTeamsMaxAmount(8);
        e.setTeamCost(150.0);
        e.setStatus(TournamentStatus.ACTIVE);
        return e;
    }
}
