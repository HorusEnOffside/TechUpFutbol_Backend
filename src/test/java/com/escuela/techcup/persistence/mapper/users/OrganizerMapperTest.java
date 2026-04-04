package com.escuela.techcup.persistence.mapper.users;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.core.model.Organizer;
import com.escuela.techcup.persistence.entity.users.OrganizerEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrganizerMapperTest {
    @Test
    void toModel_and_toEntity_areConsistent() {
        OrganizerEntity entity = new OrganizerEntity();
        entity.setId("org-1");
        entity.setName("Org Test");
        entity.setMail("org@test.com");
        entity.setDateOfBirth(LocalDate.of(1992, 2, 2));
        entity.setGender(Gender.FEMALE);
        entity.setPasswordHash("hashedpass");
        entity.setRoles(Set.of(UserRole.ORGANIZER));

        Organizer model = OrganizerMapper.toModel(entity);
        OrganizerEntity mappedEntity = OrganizerMapper.toEntity(model);

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
