package com.escuela.techcup.persistence.mapper.users;

import com.escuela.techcup.persistence.entity.users.RefereeEntity;
import com.escuela.techcup.core.model.Referee;
import java.util.UUID;

public class RefereeMapper {

    private RefereeMapper() {
    }

    public static Referee toModel(RefereeEntity entity) {
        if (entity == null) return null;
        Referee referee = new Referee(
            entity.getId().toString(),
            entity.getName(),
            entity.getMail(),
            entity.getDateOfBirth(),
            entity.getGender(),
            entity.getPasswordHash()
        );
        if (entity.getRoles() != null) {
            referee.setRoles(entity.getRoles());
        }
        return referee;
    }

    public static RefereeEntity toEntity(Referee model) {
        if (model == null) return null;
        RefereeEntity entity = new RefereeEntity();
        entity.setId(UUID.fromString(model.getId()));
        entity.setName(model.getName());
        entity.setMail(model.getMail());
        entity.setDateOfBirth(model.getDateOfBirth());
        entity.setGender(model.getGender());
        entity.setPasswordHash(model.getPassword());
        entity.setRoles(model.getRoles());
        return entity;
    }
}
