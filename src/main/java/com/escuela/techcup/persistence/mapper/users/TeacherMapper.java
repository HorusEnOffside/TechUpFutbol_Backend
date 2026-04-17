package com.escuela.techcup.persistence.mapper.users;

import com.escuela.techcup.persistence.entity.users.TeacherEntity;
import com.escuela.techcup.core.model.Teacher;
import java.util.UUID;

public class TeacherMapper {

    private TeacherMapper() {}

    public static Teacher toModel(TeacherEntity entity) {
        if (entity == null) return null;
        Teacher teacher = new Teacher(
                entity.getId().toString(),
                entity.getName(),
                entity.getMail(),
                entity.getDateOfBirth(),
                entity.getGender(),
                entity.getPasswordHash(),
                entity.getCareer()
        );
        if (entity.getRoles() != null) teacher.setRoles(entity.getRoles());
        return teacher;
    }

    public static TeacherEntity toEntity(Teacher model) {
        if (model == null) return null;
        TeacherEntity entity = new TeacherEntity();
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