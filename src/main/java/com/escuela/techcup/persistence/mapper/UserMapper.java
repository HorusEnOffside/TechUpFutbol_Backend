package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.persistence.entity.users.UserEntity;
import com.escuela.techcup.core.model.User;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import javax.imageio.ImageIO;

public class UserMapper {

    private UserMapper() {
    }


    public static User toModel(UserEntity entity) {
        if (entity == null) return null;
        BufferedImage profilePicture = null;
        if (entity.getProfilePicture() != null) {
            try {
                profilePicture = ImageIO.read(new ByteArrayInputStream(entity.getProfilePicture()));
            } catch (IOException e) {
                // handle error or leave as null
            }
        }
        User user = new User(
            entity.getId(),
            entity.getName(),
            entity.getMail(),
            entity.getDateOfBirth(),
            entity.getGender(),
            entity.getPasswordHash(),
            profilePicture
        ) {};
        user.setRoles(new HashSet<>(entity.getRoles()));
        return user;
    }

    public static void updateEntityFromModel(User model, UserEntity entity) {
        if (model == null || entity == null) return;
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setMail(model.getMail());
        entity.setDateOfBirth(model.getDateOfBirth());
        entity.setGender(model.getGender());
        entity.setPasswordHash(model.getPassword());
        entity.setRoles(model.getRoles() != null ? new HashSet<>(model.getRoles()) : new HashSet<>());
    }
}
