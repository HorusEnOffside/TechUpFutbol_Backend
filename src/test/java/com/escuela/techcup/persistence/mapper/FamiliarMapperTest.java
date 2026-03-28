package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.Familiar;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.persistence.entity.users.FamiliarEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FamiliarMapperTest {

    @Test
    void toModel_returnsCorrectId() {
        assertEquals("f1", FamiliarMapper.toModel(buildEntity()).getId());
    }

    @Test
    void toModel_returnsCorrectName() {
        assertEquals("Ana", FamiliarMapper.toModel(buildEntity()).getName());
    }

    @Test
    void toModel_returnsCorrectMail() {
        assertEquals("ana@test.com", FamiliarMapper.toModel(buildEntity()).getMail());
    }

    @Test
    void toModel_withNull_returnsNull() {
        assertNull(FamiliarMapper.toModel(null));
    }

    @Test
    void toEntity_returnsCorrectId() {
        assertEquals("f1", FamiliarMapper.toEntity(buildModel()).getId());
    }

    @Test
    void toEntity_returnsCorrectName() {
        assertEquals("Ana", FamiliarMapper.toEntity(buildModel()).getName());
    }

    @Test
    void toEntity_returnsCorrectMail() {
        assertEquals("ana@test.com", FamiliarMapper.toEntity(buildModel()).getMail());
    }

    @Test
    void toEntity_withNull_returnsNull() {
        assertNull(FamiliarMapper.toEntity(null));
    }

    @Test
    void toEntity_rolesAreMapped() {
        assertNotNull(FamiliarMapper.toEntity(buildModel()).getRoles());
    }

    private FamiliarEntity buildEntity() {
        FamiliarEntity e = new FamiliarEntity();
        e.setId("f1");
        e.setName("Ana");
        e.setMail("ana@test.com");
        e.setDateOfBirth(LocalDate.of(1995, 6, 20));
        e.setGender(Gender.FEMALE);
        e.setPasswordHash("hash");
        return e;
    }

    private Familiar buildModel() {
        return new Familiar("f1", "Ana", "ana@test.com",
                LocalDate.of(1995, 6, 20), Gender.FEMALE, "hash");
    }
}