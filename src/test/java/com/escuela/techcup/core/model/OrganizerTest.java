package com.escuela.techcup.core.model;

import com.escuela.techcup.core.model.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrganizerTest {

    private Organizer organizer;

    @BeforeEach
    void setUp() {
        organizer = new Organizer("1", "Carlos", "carlos@test.com",
                LocalDate.of(1990, 5, 15), Gender.HOMBRE, "pass123");
    }

    @Test
    void testGetId() {
        assertEquals("1", organizer.getId());
    }

    @Test
    void testGetName() {
        assertEquals("Carlos", organizer.getName());
    }

    @Test
    void testGetEmail() {
        assertEquals("carlos@test.com", organizer.getEmail());
    }

    @Test
    void testGetGender() {
        assertEquals(Gender.HOMBRE, organizer.getGender());
    }

    @Test
    void testGetDateOfBirth() {
        assertEquals(LocalDate.of(1990, 5, 15), organizer.getDateOfBirth());
    }

    @Test
    void testGetPassword() {
        assertEquals("pass123", organizer.getPassword());
    }

    @Test
    void testProfilePictureEsNulaPorDefecto() {
        assertNull(organizer.getProfilePicture());
    }

    @Test
    void testConstructorConTodosLosCampos() {
        assertNotNull(organizer);
    }

    @Test
    void testSetName() {
        organizer.setName("Pedro");
        assertEquals("Pedro", organizer.getName());
    }

    @Test
    void testSetEmail() {
        organizer.setEmail("nuevo@test.com");
        assertEquals("nuevo@test.com", organizer.getEmail());
    }

    @Test
    void testSetGender() {
        organizer.setGender(Gender.MUJER);
        assertEquals(Gender.MUJER, organizer.getGender());
    }
}
