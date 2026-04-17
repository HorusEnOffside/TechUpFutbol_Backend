package com.escuela.techcup.persistence.mapper.users;

import com.escuela.techcup.persistence.entity.users.OrganizerEntity;
import com.escuela.techcup.core.model.Organizer;

public class OrganizerMapper {

    private OrganizerMapper() {
    }

    public static Organizer toModel(OrganizerEntity entity) {
        if (entity == null) return null;
        Organizer organizer = new Organizer(
            entity.getId(),
            entity.getName(),
            entity.getMail(),
            entity.getDateOfBirth(),
            entity.getGender(),
            entity.getPasswordHash()
        );
        if (entity.getRoles() != null) {
            organizer.setRoles(entity.getRoles());
        }
        return organizer;
    }

    public static OrganizerEntity toEntity(Organizer model) {
        if (model == null) return null;
        OrganizerEntity entity = new OrganizerEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setMail(model.getMail());
        entity.setDateOfBirth(model.getDateOfBirth());
        entity.setGender(model.getGender());
        entity.setPasswordHash(model.getPassword());
        entity.setRoles(model.getRoles());
        return entity;
    }
}
