package com.escuela.techcup.core.model;

import com.escuela.techcup.core.model.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("s1", "Juan", "juan@test.com",
                LocalDate.of(2003, 8, 22), Gender.HOMBRE, 5, "pass789");
    }

    @Test
    void testGetId() {
        assertEquals("s1", student.getId());
    }

    @Test
    void testGetName() {
        assertEquals("Juan", student.getName());
    }

    @Test
    void testGetEmail() {
        assertEquals("juan@test.com", student.getMail());
    }

    @Test
    void testGetGender() {
        assertEquals(Gender.HOMBRE, student.getGender());
    }

    @Test
    void testGetDateOfBirth() {
        assertEquals(LocalDate.of(2003, 8, 22), student.getDateOfBirth());
    }

    @Test
    void testGetSemester() {
        assertEquals(5, student.getSemester());
    }

    @Test
    void testGetPassword() {
        assertEquals("pass789", student.getPassword());
    }

    @Test
    void testProfilePictureEsNulaPorDefecto() {
        assertNull(student.getProfilePicture());
    }

    @Test
    void testSetSemester() {
        student.setSemester(8);
        assertEquals(8, student.getSemester());
    }

    @Test
    void testSetName() {
        student.setName("Carlos");
        assertEquals("Carlos", student.getName());
    }

    @Test
    void testSemesterCero() {
        Student s = new Student("s2", "Test", "t@test.com",
                LocalDate.of(2005, 1, 1), Gender.MUJER, 0, "p");
        assertEquals(0, s.getSemester());
    }

    @Test
    void testSemesterPrimerSemestre() {
        Student s = new Student("s3", "Nuevo", "n@test.com",
                LocalDate.of(2006, 2, 2), Gender.HOMBRE, 1, "p");
        assertEquals(1, s.getSemester());
    }
}
