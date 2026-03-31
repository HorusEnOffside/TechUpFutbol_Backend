package com.escuela.techcup.persistence.mapper.users;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.core.model.Student;
import com.escuela.techcup.persistence.entity.users.StudentEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {
    @Test
    void toModel_and_toEntity_areConsistent() {
        StudentEntity entity = new StudentEntity();
        entity.setId("stud-1");
        entity.setName("Stud Test");
        entity.setMail("stud@test.com");
        entity.setDateOfBirth(LocalDate.of(1997, 7, 7));
        entity.setGender(Gender.MALE);
        entity.setPasswordHash("hashedpass");
        entity.setRoles(Set.of(UserRole.BASEUSER));
        entity.setSemester(1);
        entity.setSemester(1);

        Student model = StudentMapper.toModel(entity);
        StudentEntity mappedEntity = StudentMapper.toEntity(model);

        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getMail(), model.getMail());
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getDateOfBirth(), model.getDateOfBirth());
        assertEquals(entity.getGender(), model.getGender());
        assertEquals(entity.getPasswordHash(), model.getPassword());
        assertEquals(entity.getRoles(), model.getRoles());
        assertEquals(entity.getSemester(), model.getSemester());

        assertEquals(entity.getId(), mappedEntity.getId());
        assertEquals(entity.getMail(), mappedEntity.getMail());
        assertEquals(entity.getName(), mappedEntity.getName());
        assertEquals(entity.getDateOfBirth(), mappedEntity.getDateOfBirth());
        assertEquals(entity.getGender(), mappedEntity.getGender());
        assertEquals(entity.getPasswordHash(), mappedEntity.getPasswordHash());
        assertEquals(entity.getRoles(), mappedEntity.getRoles());
        assertEquals(entity.getSemester(), mappedEntity.getSemester());
    }
}
