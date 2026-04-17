package com.escuela.techcup.persistence.mapper.tournament;

import com.escuela.techcup.core.model.SoccerField;
import com.escuela.techcup.persistence.entity.tournament.SoccerFieldEntity;
import java.util.UUID;

public class SoccerFieldMapper {
    private SoccerFieldMapper() {}

    public static SoccerFieldEntity toEntity(SoccerField model) {
        SoccerFieldEntity entity = new SoccerFieldEntity();
        entity.setId(UUID.fromString(model.getId()));
        entity.setName(model.getName());
        entity.setLocation(model.getLocation());
        entity.setFoto(model.getFoto());
        return entity;
    }

    public static SoccerField toModel(SoccerFieldEntity entity) {
        SoccerField model = new SoccerField(entity.getId().toString(), entity.getName(), entity.getLocation());
        model.setFoto(entity.getFoto());
        return model;
    }
}
