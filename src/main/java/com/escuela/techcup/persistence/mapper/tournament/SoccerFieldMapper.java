package com.escuela.techcup.persistence.mapper.tournament;

import com.escuela.techcup.core.model.SoccerField;
import com.escuela.techcup.persistence.entity.tournament.SoccerFieldEntity;

public class SoccerFieldMapper {
    private SoccerFieldMapper() {}

    public static SoccerFieldEntity toEntity(SoccerField model) {
        SoccerFieldEntity entity = new SoccerFieldEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setLocation(model.getLocation());
        return entity;
    }

    public static SoccerField toModel(SoccerFieldEntity entity) {
        return new SoccerField(entity.getId(), entity.getName(), entity.getLocation());
    }
}
