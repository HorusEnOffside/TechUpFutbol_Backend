package com.escuela.techcup.persistence.mapper.users;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserPlayerMapperTest {
    @Test
    void toModel_and_toEntity_areConsistent() {
        UserPlayerEntity entity = new UserPlayerEntity();
        entity.setId("up-1");
        entity.setName("UserPlayer Test");
        entity.setMail("up@test.com");
        entity.setDateOfBirth(LocalDate.of(1998, 8, 8));
        entity.setGender(Gender.FEMALE);
        entity.setPasswordHash("hashedpass");
        entity.setRoles(Set.of(UserRole.PLAYER));

        UserPlayer model = UserPlayerMapper.toModel(entity);
        UserPlayerEntity mappedEntity = UserPlayerMapper.toEntity(model);

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
