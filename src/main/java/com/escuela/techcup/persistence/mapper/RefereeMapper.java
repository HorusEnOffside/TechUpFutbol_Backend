package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.persistence.entity.users.RefereeEntity;
import com.escuela.techcup.core.model.Referee;

public class RefereeMapper {

    private RefereeMapper() {
    }

    public static Referee toModel(RefereeEntity entity) {
        if (entity == null) return null;
        return new Referee(
            entity.getId(),
            entity.getName(),
            entity.getMail(),
            entity.getDateOfBirth(),
            entity.getGender(),
            entity.getPasswordHash()
        );
    }

    public static RefereeEntity toEntity(Referee model) {
        if (model == null) return null;
        RefereeEntity entity = new RefereeEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setMail(model.getMail());
        entity.setDateOfBirth(model.getDateOfBirth());
        entity.setGender(model.getGender());
        entity.setPasswordHash(model.getPassword());
        entity.setRoles(model.getRoles());
        return entity;
    }
}
