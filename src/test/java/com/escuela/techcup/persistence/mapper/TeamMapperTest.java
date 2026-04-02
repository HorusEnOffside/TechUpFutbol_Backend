package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamMapperTest {

    @Test
    void toModel_returnsCorrectId() {
        assertEquals("t1", TeamMapper.toModel(buildEntity()).getId());
    }

    @Test
    void toModel_returnsCorrectName() {
        assertEquals("Los Tigres", TeamMapper.toModel(buildEntity()).getName());
    }

    @Test
    void toModel_returnsCorrectUniformColor() {
        assertEquals("Rojo y Negro", TeamMapper.toModel(buildEntity()).getUniformColor());
    }

    @Test
    void toModel_withNull_returnsNull() {
        assertNull(TeamMapper.toModel(null));
    }

    @Test
    void toEntity_returnsCorrectId() {
        assertEquals("t1", TeamMapper.toEntity(buildModel()).getId());
    }

    @Test
    void toEntity_returnsCorrectName() {
        assertEquals("Los Tigres", TeamMapper.toEntity(buildModel()).getName());
    }

    @Test
    void toEntity_returnsCorrectUniformColor() {
        assertEquals("Rojo y Negro", TeamMapper.toEntity(buildModel()).getUniformColor());
    }

    @Test
    void toEntity_withNull_returnsNull() {
        assertNull(TeamMapper.toEntity(null));
    }

    private TeamEntity buildEntity() {
        TeamEntity e = new TeamEntity();
        e.setId("t1");
        e.setName("Los Tigres");
        e.setUniformColor("Rojo y Negro");
        return e;
    }

    private Team buildModel() {
        return new Team("t1", "Los Tigres", "Rojo y Negro", null);
    }
}