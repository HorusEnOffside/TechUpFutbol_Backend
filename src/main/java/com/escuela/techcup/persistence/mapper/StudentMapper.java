package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.Student;
import com.escuela.techcup.persistence.entity.users.StudentEntity;

public class StudentMapper {
        private StudentMapper() {
    }

    public static Student toModel(StudentEntity entity) {
        if (entity == null) return null;
        return new Student(
            entity.getId(),
            entity.getName(),
            entity.getMail(),
            entity.getDateOfBirth(),
            entity.getGender(),
            entity.getSemester(),
            entity.getPasswordHash()
        );
    }

    public static StudentEntity toEntity(Student model) {
        if (model == null) return null;
        StudentEntity entity = new StudentEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setMail(model.getMail());
        entity.setDateOfBirth(model.getDateOfBirth());
        entity.setGender(model.getGender());
        entity.setSemester(model.getSemester());
        entity.setPasswordHash(model.getPassword());
        entity.setRoles(model.getRoles());
        return entity;
    }
}
