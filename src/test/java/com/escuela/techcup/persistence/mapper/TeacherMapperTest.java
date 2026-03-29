package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.Teacher;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.persistence.entity.users.TeacherEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TeacherMapperTest {

    @Test
    void toModel_returnsCorrectId() {
        assertEquals("t1", TeacherMapper.toModel(buildEntity()).getId());
    }

    @Test
    void toModel_returnsCorrectName() {
        assertEquals("Sofia", TeacherMapper.toModel(buildEntity()).getName());
    }

    @Test
    void toModel_returnsCorrectMail() {
        assertEquals("sofia@test.com", TeacherMapper.toModel(buildEntity()).getMail());
    }

    @Test
    void toModel_withNull_returnsNull() {
        assertNull(TeacherMapper.toModel(null));
    }

    @Test
    void toEntity_returnsCorrectId() {
        assertEquals("t1", TeacherMapper.toEntity(buildModel()).getId());
    }

    @Test
    void toEntity_returnsCorrectName() {
        assertEquals("Sofia", TeacherMapper.toEntity(buildModel()).getName());
    }

    @Test
    void toEntity_returnsCorrectMail() {
        assertEquals("sofia@test.com", TeacherMapper.toEntity(buildModel()).getMail());
    }

    @Test
    void toEntity_withNull_returnsNull() {
        assertNull(TeacherMapper.toEntity(null));
    }

    @Test
    void toEntity_rolesAreMapped() {
        assertNotNull(TeacherMapper.toEntity(buildModel()).getRoles());
    }

    private TeacherEntity buildEntity() {
        TeacherEntity e = new TeacherEntity();
        e.setId("t1");
        e.setName("Sofia");
        e.setMail("sofia@test.com");
        e.setDateOfBirth(LocalDate.of(1980, 9, 25));
        e.setGender(Gender.FEMALE);
        e.setPasswordHash("hash");
        return e;
    }

    private Teacher buildModel() {
        return new Teacher("t1", "Sofia", "sofia@test.com",
                LocalDate.of(1980, 9, 25), Gender.FEMALE, "hash");
    }
}