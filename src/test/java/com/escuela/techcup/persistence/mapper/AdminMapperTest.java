package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.Administrator;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.persistence.entity.users.AdministratorEntity;
import com.escuela.techcup.persistence.mapper.users.AdminMapper;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AdminMapperTest {

    @Test
    void toModel_returnsCorrectId() {
        AdministratorEntity entity = buildEntity();
        Administrator model = AdminMapper.toModel(entity);
        assertEquals("id-1", model.getId());
    }

    @Test
    void toModel_returnsCorrectName() {
        assertEquals("Carlos", AdminMapper.toModel(buildEntity()).getName());
    }

    @Test
    void toModel_returnsCorrectMail() {
        assertEquals("carlos@test.com", AdminMapper.toModel(buildEntity()).getMail());
    }

    @Test
    void toModel_withNull_returnsNull() {
        assertNull(AdminMapper.toModel(null));
    }

    @Test
    void toEntity_returnsCorrectId() {
        AdministratorEntity entity = AdminMapper.toEntity(buildModel());
        assertEquals("id-1", entity.getId());
    }

    @Test
    void toEntity_returnsCorrectName() {
        assertEquals("Carlos", AdminMapper.toEntity(buildModel()).getName());
    }

    @Test
    void toEntity_returnsCorrectMail() {
        assertEquals("carlos@test.com", AdminMapper.toEntity(buildModel()).getMail());
    }

    @Test
    void toEntity_withNull_returnsNull() {
        assertNull(AdminMapper.toEntity(null));
    }

    @Test
    void toEntity_rolesAreMapped() {
        AdministratorEntity entity = AdminMapper.toEntity(buildModel());
        assertNotNull(entity.getRoles());
    }

    private AdministratorEntity buildEntity() {
        AdministratorEntity e = new AdministratorEntity();
        e.setId("id-1");
        e.setName("Carlos");
        e.setMail("carlos@test.com");
        e.setDateOfBirth(LocalDate.of(1990, 1, 1));
        e.setGender(Gender.MALE);
        e.setPasswordHash("hash");
        e.setRoles(Set.of(UserRole.ADMIN));
        return e;
    }

    private Administrator buildModel() {
        return new Administrator("id-1", "Carlos", "carlos@test.com",
                LocalDate.of(1990, 1, 1), Gender.MALE, "hash");
    }
}