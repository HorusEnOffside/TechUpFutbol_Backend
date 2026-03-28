package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.persistence.entity.users.TeacherEntity;
import com.escuela.techcup.core.model.Teacher;

public class TeacherMapper {

    private TeacherMapper() {
    }

    public static Teacher toModel(TeacherEntity entity) {
        if (entity == null) return null;
        return new Teacher(
            entity.getId(),
            entity.getName(),
            entity.getMail(),
            entity.getDateOfBirth(),
            entity.getGender(),
            entity.getPasswordHash()
        );
    }

    public static TeacherEntity toEntity(Teacher model) {
        if (model == null) return null;
        TeacherEntity entity = new TeacherEntity();
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
