package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.Student;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.persistence.entity.users.StudentEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {

    @Test
    void toModel_returnsCorrectId() {
        assertEquals("s1", StudentMapper.toModel(buildEntity()).getId());
    }

    @Test
    void toModel_returnsCorrectSemester() {
        assertEquals(5, StudentMapper.toModel(buildEntity()).getSemester());
    }

    @Test
    void toModel_withNull_returnsNull() {
        assertNull(StudentMapper.toModel(null));
    }

    @Test
    void toEntity_returnsCorrectId() {
        assertEquals("s1", StudentMapper.toEntity(buildModel()).getId());
    }

    @Test
    void toEntity_returnsCorrectSemester() {
        assertEquals(5, StudentMapper.toEntity(buildModel()).getSemester());
    }

    @Test
    void toEntity_withNull_returnsNull() {
        assertNull(StudentMapper.toEntity(null));
    }

    private StudentEntity buildEntity() {
        StudentEntity e = new StudentEntity();
        e.setId("s1");
        e.setName("Juan");
        e.setMail("juan@test.com");
        e.setDateOfBirth(LocalDate.of(2003, 1, 1));
        e.setGender(Gender.MALE);
        e.setPasswordHash("hash");
        e.setSemester(5);
        return e;
    }

    private Student buildModel() {
        return new Student("s1", "Juan", "juan@test.com",
                LocalDate.of(2003, 1, 1), Gender.MALE, 5, "hash");
    }
}