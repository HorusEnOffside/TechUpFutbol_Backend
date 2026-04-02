package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;

public class TeamMapper {

    private TeamMapper() {}

    public static Team toModel(TeamEntity entity) {
        if (entity == null) return null;
        Team team = new Team(
                entity.getId(),
                entity.getName(),
                entity.getUniformColor(),
                null
        );
        return team;
    }

    public static TeamEntity toEntity(Team model) {
        if (model == null) return null;
        TeamEntity entity = new TeamEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setUniformColor(model.getUniformColor());
        return entity;
    }
}