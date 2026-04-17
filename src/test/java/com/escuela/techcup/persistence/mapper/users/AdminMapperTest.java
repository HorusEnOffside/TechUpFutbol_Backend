package com.escuela.techcup.persistence.mapper.users;

import com.escuela.techcup.core.model.Administrator;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.persistence.entity.users.AdministratorEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AdminMapperTest {
    @Test
    void toModel_and_toEntity_areConsistent() {
        // Arrange
        AdministratorEntity entity = new AdministratorEntity();
        entity.setId("admin-1");
        entity.setName("Admin Test");
        entity.setMail("admin@test.com");
        entity.setDateOfBirth(LocalDate.of(1990, 1, 1));
        entity.setGender(Gender.MALE);
        entity.setPasswordHash("hashedpass");
        entity.setRoles(Set.of(UserRole.ADMIN));

        // Act
        Administrator model = AdminMapper.toModel(entity);
        AdministratorEntity mappedEntity = AdminMapper.toEntity(model);

        // Assert
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getMail(), model.getMail());
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getDateOfBirth(), model.getDateOfBirth());
        assertEquals(entity.getGender(), model.getGender());
        assertEquals(entity.getPasswordHash(), model.getPassword());
        assertEquals(entity.getRoles(), model.getRoles());

        assertEquals(entity.getId(), mappedEntity.getId());
        assertEquals(entity.getMail(), mappedEntity.getMail());
        assertEquals(entity.getName(), mappedEntity.getName());
        assertEquals(entity.getDateOfBirth(), mappedEntity.getDateOfBirth());
        assertEquals(entity.getGender(), mappedEntity.getGender());
        assertEquals(entity.getPasswordHash(), mappedEntity.getPasswordHash());
        assertEquals(entity.getRoles(), mappedEntity.getRoles());
    }
}
