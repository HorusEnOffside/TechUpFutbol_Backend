package com.escuela.techcup.persistence.mapper.users;

import com.escuela.techcup.persistence.entity.users.AdministratorEntity;
import com.escuela.techcup.core.model.Administrator;

public class AdminMapper {

    private AdminMapper() {
    }

    public static Administrator toModel(AdministratorEntity entity) {
        if (entity == null) return null;
        Administrator admin = new Administrator(
            entity.getId(),
            entity.getName(),
            entity.getMail(),
            entity.getDateOfBirth(),
            entity.getGender(),
            entity.getPasswordHash()
        );
        if (entity.getRoles() != null) {
            admin.setRoles(entity.getRoles());
        }
        return admin;
    }

    public static AdministratorEntity toEntity(Administrator model) {
        if (model == null) return null;
        AdministratorEntity entity = new AdministratorEntity();
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
