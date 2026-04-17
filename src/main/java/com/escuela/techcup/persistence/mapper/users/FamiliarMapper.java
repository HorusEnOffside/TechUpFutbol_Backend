package com.escuela.techcup.persistence.mapper.users;

import com.escuela.techcup.persistence.entity.users.FamiliarEntity;
import com.escuela.techcup.core.model.Familiar;
import java.util.UUID;

public class FamiliarMapper {

    private FamiliarMapper() {
    }

    public static Familiar toModel(FamiliarEntity entity) {
        if (entity == null) return null;
        Familiar familiar = new Familiar(
            entity.getId().toString(),
            entity.getName(),
            entity.getMail(),
            entity.getDateOfBirth(),
            entity.getGender(),
            entity.getPasswordHash()
        );
        if (entity.getRoles() != null) {
            familiar.setRoles(entity.getRoles());
        }
        return familiar;
    }

    public static FamiliarEntity toEntity(Familiar model) {
        if (model == null) return null;
        FamiliarEntity entity = new FamiliarEntity();
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
