package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.persistence.entity.users.FamiliarEntity;
import com.escuela.techcup.core.model.Familiar;

public class FamiliarMapper {

    private FamiliarMapper() {
    }

    public static Familiar toModel(FamiliarEntity entity) {
        if (entity == null) return null;
        return new Familiar(
            entity.getId(),
            entity.getName(),
            entity.getMail(),
            entity.getDateOfBirth(),
            entity.getGender(),
            entity.getPasswordHash()
        );
    }

    public static FamiliarEntity toEntity(Familiar model) {
        if (model == null) return null;
        FamiliarEntity entity = new FamiliarEntity();
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
