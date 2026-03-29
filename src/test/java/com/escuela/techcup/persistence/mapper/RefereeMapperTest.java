package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.Referee;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.persistence.entity.users.RefereeEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RefereeMapperTest {

    @Test
    void toModel_returnsCorrectId() {
        assertEquals("r1", RefereeMapper.toModel(buildEntity()).getId());
    }

    @Test
    void toModel_returnsCorrectName() {
        assertEquals("Roberto", RefereeMapper.toModel(buildEntity()).getName());
    }

    @Test
    void toModel_returnsCorrectMail() {
        assertEquals("roberto@test.com", RefereeMapper.toModel(buildEntity()).getMail());
    }

    @Test
    void toModel_withNull_returnsNull() {
        assertNull(RefereeMapper.toModel(null));
    }

    @Test
    void toEntity_returnsCorrectId() {
        assertEquals("r1", RefereeMapper.toEntity(buildModel()).getId());
    }

    @Test
    void toEntity_returnsCorrectName() {
        assertEquals("Roberto", RefereeMapper.toEntity(buildModel()).getName());
    }

    @Test
    void toEntity_returnsCorrectMail() {
        assertEquals("roberto@test.com", RefereeMapper.toEntity(buildModel()).getMail());
    }

    @Test
    void toEntity_withNull_returnsNull() {
        assertNull(RefereeMapper.toEntity(null));
    }

    @Test
    void toEntity_rolesAreMapped() {
        assertNotNull(RefereeMapper.toEntity(buildModel()).getRoles());
    }

    private RefereeEntity buildEntity() {
        RefereeEntity e = new RefereeEntity();
        e.setId("r1");
        e.setName("Roberto");
        e.setMail("roberto@test.com");
        e.setDateOfBirth(LocalDate.of(1985, 7, 15));
        e.setGender(Gender.MALE);
        e.setPasswordHash("hash");
        return e;
    }

    private Referee buildModel() {
        return new Referee("r1", "Roberto", "roberto@test.com",
                LocalDate.of(1985, 7, 15), Gender.MALE, "hash");
    }
}