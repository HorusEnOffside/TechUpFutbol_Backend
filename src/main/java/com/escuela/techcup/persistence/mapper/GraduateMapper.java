package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.persistence.entity.users.GraduateEntity;
import com.escuela.techcup.core.model.Graduate;

public class GraduateMapper {

    private GraduateMapper() {
    }

    public static Graduate toModel(GraduateEntity entity) {
        if (entity == null) return null;
        return new Graduate(
            entity.getId(),
            entity.getName(),
            entity.getMail(),
            entity.getDateOfBirth(),
            entity.getGender(),
            entity.getPasswordHash()
        );
    }

    public static GraduateEntity toEntity(Graduate model) {
        if (model == null) return null;
        GraduateEntity entity = new GraduateEntity();
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
