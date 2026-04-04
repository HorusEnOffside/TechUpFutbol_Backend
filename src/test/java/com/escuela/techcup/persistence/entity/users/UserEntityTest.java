package com.escuela.techcup.persistence.entity.users;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;
import java.util.Set;

class UserEntityTest {
    @Test
    void canSetAndGetFields() {
        // Anonymous subclass since UserEntity is abstract
        UserEntity entity = new UserEntity() {};
        entity.setId("id1");
        entity.setName("Test User");
        entity.setMail("test@mail.com");
        entity.setDateOfBirth(LocalDate.of(2000, 1, 1));
        entity.setGender(Gender.MALE);
        entity.setPasswordHash("hash");
        entity.setRoles(Set.of(UserRole.ADMIN, UserRole.BASEUSER));
        assertEquals("id1", entity.getId());
        assertEquals("Test User", entity.getName());
        assertEquals("test@mail.com", entity.getMail());
        assertEquals(LocalDate.of(2000, 1, 1), entity.getDateOfBirth());
        assertEquals(Gender.MALE, entity.getGender());
        assertEquals("hash", entity.getPasswordHash());
        assertTrue(entity.getRoles().contains(UserRole.ADMIN));
        assertTrue(entity.getRoles().contains(UserRole.BASEUSER));
    }
}
