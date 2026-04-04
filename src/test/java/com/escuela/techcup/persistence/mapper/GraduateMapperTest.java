package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.Graduate;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.persistence.entity.users.GraduateEntity;
import com.escuela.techcup.persistence.mapper.users.GraduateMapper;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GraduateMapperTest {

    @Test
    void toModel_returnsCorrectId() {
        assertEquals("g1", GraduateMapper.toModel(buildEntity()).getId());
    }

    @Test
    void toModel_returnsCorrectName() {
        assertEquals("Luis", GraduateMapper.toModel(buildEntity()).getName());
    }

    @Test
    void toModel_returnsCorrectMail() {
        assertEquals("luis@test.com", GraduateMapper.toModel(buildEntity()).getMail());
    }

    @Test
    void toModel_withNull_returnsNull() {
        assertNull(GraduateMapper.toModel(null));
    }

    @Test
    void toEntity_returnsCorrectId() {
        assertEquals("g1", GraduateMapper.toEntity(buildModel()).getId());
    }

    @Test
    void toEntity_returnsCorrectName() {
        assertEquals("Luis", GraduateMapper.toEntity(buildModel()).getName());
    }

    @Test
    void toEntity_returnsCorrectMail() {
        assertEquals("luis@test.com", GraduateMapper.toEntity(buildModel()).getMail());
    }

    @Test
    void toEntity_withNull_returnsNull() {
        assertNull(GraduateMapper.toEntity(null));
    }

    @Test
    void toEntity_rolesAreMapped() {
        assertNotNull(GraduateMapper.toEntity(buildModel()).getRoles());
    }

    private GraduateEntity buildEntity() {
        GraduateEntity e = new GraduateEntity();
        e.setId("g1");
        e.setName("Luis");
        e.setMail("luis@test.com");
        e.setDateOfBirth(LocalDate.of(1988, 4, 5));
        e.setGender(Gender.MALE);
        e.setPasswordHash("hash");
        return e;
    }

    private Graduate buildModel() {
        return new Graduate("g1", "Luis", "luis@test.com",
                LocalDate.of(1988, 4, 5), Gender.MALE, "hash");
    }
}