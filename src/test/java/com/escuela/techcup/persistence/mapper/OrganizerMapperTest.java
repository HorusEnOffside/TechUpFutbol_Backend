package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.Organizer;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.persistence.entity.users.OrganizerEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrganizerMapperTest {

    @Test
    void toModel_returnsCorrectId() {
        assertEquals("o1", OrganizerMapper.toModel(buildEntity()).getId());
    }

    @Test
    void toModel_returnsCorrectName() {
        assertEquals("Maria", OrganizerMapper.toModel(buildEntity()).getName());
    }

    @Test
    void toModel_returnsCorrectMail() {
        assertEquals("maria@test.com", OrganizerMapper.toModel(buildEntity()).getMail());
    }

    @Test
    void toModel_withNull_returnsNull() {
        assertNull(OrganizerMapper.toModel(null));
    }

    @Test
    void toEntity_returnsCorrectId() {
        assertEquals("o1", OrganizerMapper.toEntity(buildModel()).getId());
    }

    @Test
    void toEntity_returnsCorrectName() {
        assertEquals("Maria", OrganizerMapper.toEntity(buildModel()).getName());
    }

    @Test
    void toEntity_returnsCorrectMail() {
        assertEquals("maria@test.com", OrganizerMapper.toEntity(buildModel()).getMail());
    }

    @Test
    void toEntity_withNull_returnsNull() {
        assertNull(OrganizerMapper.toEntity(null));
    }

    @Test
    void toEntity_rolesAreMapped() {
        assertNotNull(OrganizerMapper.toEntity(buildModel()).getRoles());
    }

    private OrganizerEntity buildEntity() {
        OrganizerEntity e = new OrganizerEntity();
        e.setId("o1");
        e.setName("Maria");
        e.setMail("maria@test.com");
        e.setDateOfBirth(LocalDate.of(1992, 3, 10));
        e.setGender(Gender.FEMALE);
        e.setPasswordHash("hash");
        return e;
    }

    private Organizer buildModel() {
        return new Organizer("o1", "Maria", "maria@test.com",
                LocalDate.of(1992, 3, 10), Gender.FEMALE, "hash");
    }
}