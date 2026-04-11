package com.escuela.techcup.persistence.mapper.users;

import com.escuela.techcup.core.model.Student;
import com.escuela.techcup.persistence.entity.users.StudentEntity;

public class StudentMapper {

    private StudentMapper() {}

    public static Student toModel(StudentEntity entity) {
        if (entity == null) return null;
        Student student = new Student(
                entity.getId(),
                entity.getName(),
                entity.getMail(),
                entity.getDateOfBirth(),
                entity.getGender(),
                entity.getSemester(),
                entity.getCareer(),
                entity.getPasswordHash()
        );
        if (entity.getRoles() != null) student.setRoles(entity.getRoles());
        return student;
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
        entity.setCareer(model.getCareer());
        entity.setPasswordHash(model.getPassword());
        entity.setRoles(model.getRoles());
        return entity;
    }
}