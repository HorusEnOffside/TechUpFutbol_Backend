package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;
import com.escuela.techcup.core.model.UserPlayer;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class UserPlayerMapper {

    private UserPlayerMapper() {
    }
    
    public static UserPlayer toModel(UserPlayerEntity entity) {
        if (entity == null) return null;
        BufferedImage profilePicture = null;
        if (entity.getProfilePicture() != null) {
            try {
                profilePicture = ImageIO.read(new ByteArrayInputStream(entity.getProfilePicture()));
            } catch (IOException e) {
                // handle error or leave as null
            }
        }
        return new UserPlayer(
            entity.getId(),
            entity.getName(),
            entity.getMail(),
            entity.getDateOfBirth(),
            entity.getGender(),
            entity.getPasswordHash(),
            profilePicture
        );
    }

    public static UserPlayerEntity toEntity(UserPlayer model) {
        if (model == null) return null;
        UserPlayerEntity entity = new UserPlayerEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setMail(model.getMail());
        entity.setDateOfBirth(model.getDateOfBirth());
        entity.setGender(model.getGender());
        entity.setPasswordHash(model.getPassword());
        return entity;
    }
}
