package com.escuela.techcup.core.model;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class OrganizerTest {

    private Organizer organizer;

    @BeforeEach
    void setUp() {
        organizer = new Organizer("1", "Carlos", "carlos@test.com",
                LocalDate.of(1990, 5, 15), Gender.MALE, "pass123");
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
    void testGetMail() {
        assertEquals("carlos@test.com", organizer.getMail());
    }

    @Test
    void testGetGender() {
        assertEquals(Gender.MALE, organizer.getGender());
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
    void testRolesPorDefectoContienenBASEUSER() {
        assertTrue(organizer.getRoles().contains(UserRole.BASEUSER));
    }

    @Test
    void testRolesPorDefectoTieneUnSoloRol() {
        assertEquals(1, organizer.getRoles().size());
    }

    @Test
    void testGetRoles_noEsNulo() {
        assertNotNull(organizer.getRoles());
    }

    @Test
    void testSetPrimaryRole() {
        organizer.setPrimaryRole(UserRole.ORGANIZER);
        assertTrue(organizer.getRoles().contains(UserRole.ORGANIZER));
    }

    @Test
    void testSetPrimaryRole_reemplazaRolAnterior() {
        organizer.setPrimaryRole(UserRole.ORGANIZER);
        assertFalse(organizer.getRoles().contains(UserRole.BASEUSER));
    }

    @Test
    void testSetPrimaryRole_soloTieneUnRol() {
        organizer.setPrimaryRole(UserRole.ORGANIZER);
        assertEquals(1, organizer.getRoles().size());
    }

    @Test
    void testAddRole() {
        organizer.addRole(UserRole.ORGANIZER);
        assertTrue(organizer.getRoles().contains(UserRole.ORGANIZER));
    }

    @Test
    void testAddRole_conservaRolAnterior() {
        organizer.addRole(UserRole.ORGANIZER);
        assertTrue(organizer.getRoles().contains(UserRole.BASEUSER));
    }

    @Test
    void testAddRole_tieneDoRoles() {
        organizer.addRole(UserRole.ORGANIZER);
        assertEquals(2, organizer.getRoles().size());
    }

    @Test
    void testAddMultiplesRoles() {
        organizer.addRole(UserRole.ORGANIZER);
        organizer.addRole(UserRole.ADMIN);
        assertEquals(3, organizer.getRoles().size());
    }

    @Test
    void testAddRoleDuplicadoNoLoAgregaDosVeces() {
        organizer.addRole(UserRole.BASEUSER);
        assertEquals(1, organizer.getRoles().size());
    }

    @Test
    void testSetId() {
        organizer.setId("99");
        assertEquals("99", organizer.getId());
    }

    @Test
    void testSetName() {
        organizer.setName("Pedro");
        assertEquals("Pedro", organizer.getName());
    }

    @Test
    void testSetMail() {
        organizer.setMail("nuevo@test.com");
        assertEquals("nuevo@test.com", organizer.getMail());
    }

    @Test
    void testSetGender() {
        organizer.setGender(Gender.FEMALE);
        assertEquals(Gender.FEMALE, organizer.getGender());
    }

    @Test
    void testSetDateOfBirth() {
        organizer.setDateOfBirth(LocalDate.of(2000, 1, 1));
        assertEquals(LocalDate.of(2000, 1, 1), organizer.getDateOfBirth());
    }

    @Test
    void testSetPassword() {
        organizer.setPassword("nuevaPass");
        assertEquals("nuevaPass", organizer.getPassword());
    }

    @Test
    void testSetRoles() {
        organizer.setRoles(EnumSet.of(UserRole.ADMIN));
        assertTrue(organizer.getRoles().contains(UserRole.ADMIN));
    }

    @Test
    void testSetProfilePicture() {
        BufferedImage imagen = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        organizer.setProfilePicture(imagen);
        assertNotNull(organizer.getProfilePicture());
    }

    @Test
    void testEquals_mismoObjeto() {
        assertEquals(organizer, organizer);
    }

    @Test
    void testEquals_objetosIguales() {
        Organizer o2 = new Organizer("1", "Carlos", "carlos@test.com",
                LocalDate.of(1990, 5, 15), Gender.MALE, "pass123");
        assertEquals(organizer, o2);
    }

    @Test
    void testEquals_objetosDiferentes() {
        Organizer o2 = new Organizer("2", "Pedro", "pedro@test.com",
                LocalDate.of(1985, 1, 1), Gender.MALE, "pass");
        assertNotEquals(organizer, o2);
    }

    @Test
    void testEquals_conNulo() {
        assertNotEquals(null, organizer);
    }

    @Test
    void testEquals_distintaClase() {
        assertNotEquals("un string cualquiera", organizer);
    }

    @Test
    void testHashCode_objetosIguales() {
        Organizer o2 = new Organizer("1", "Carlos", "carlos@test.com",
                LocalDate.of(1990, 5, 15), Gender.MALE, "pass123");
        assertEquals(organizer.hashCode(), o2.hashCode());
    }

    @Test
    void testHashCode_objetosDiferentes() {
        Organizer o2 = new Organizer("2", "Pedro", "pedro@test.com",
                LocalDate.of(1985, 1, 1), Gender.MALE, "pass");
        assertNotEquals(organizer.hashCode(), o2.hashCode());
    }

    @Test
    void testToString_noEsNulo() {
        assertNotNull(organizer.toString());
    }

    @Test
    void testToString_contieneNombre() {
        assertTrue(organizer.toString().contains("Carlos"));
    }

    @Test
    void testToString_contieneMail() {
        assertTrue(organizer.toString().contains("carlos@test.com"));
    }
}
