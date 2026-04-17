package com.escuela.techcup.persistence.mapper.users;

import com.escuela.techcup.persistence.entity.users.GraduateEntity;
import com.escuela.techcup.core.model.Graduate;
import java.util.UUID;

public class GraduateMapper {

    private GraduateMapper() {}

    public static Graduate toModel(GraduateEntity entity) {
        if (entity == null) return null;
        Graduate graduate = new Graduate(
                entity.getId().toString(),
                entity.getName(),
                entity.getMail(),
                entity.getDateOfBirth(),
                entity.getGender(),
                entity.getPasswordHash(),
                entity.getCareer()
        );
        if (entity.getRoles() != null) graduate.setRoles(entity.getRoles());
        return graduate;
    }

    public static GraduateEntity toEntity(Graduate model) {
        if (model == null) return null;
        GraduateEntity entity = new GraduateEntity();
        entity.setId(UUID.fromString(model.getId()));
        entity.setName(model.getName());
        entity.setMail(model.getMail());
        entity.setDateOfBirth(model.getDateOfBirth());
        entity.setGender(model.getGender());
        entity.setPasswordHash(model.getPassword());
        entity.setCareer(model.getCareer());
        entity.setRoles(model.getRoles());
        return entity;
    }
}