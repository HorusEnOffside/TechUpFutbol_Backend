package com.escuela.techcup.persistence.mapper.users;

import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.core.model.ComponentPlayer;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;
import java.util.UUID;

public class PlayerMapper {

    private PlayerMapper() {
    }
    
    public static Player toModel(PlayerEntity entity) {
        if (entity == null) return null;
        UserPlayer userPlayer = UserPlayerMapper.toModel(entity.getUser());
        // Copiar roles del entity UserPlayerEntity al modelo UserPlayer
        if (entity.getUser() != null && entity.getUser().getRoles() != null) {
            userPlayer.setRoles(entity.getUser().getRoles());
        }
        Player player = new Player(
            userPlayer,
            entity.getPosition(),
            entity.getDorsalNumber()
        );
        player.setStatus(entity.getStatus());
        return player;
    }

    public static PlayerEntity toEntity(Player model) {
        if (model == null) return null;
        PlayerEntity entity = new PlayerEntity();
        entity.setId(UUID.fromString(model.getUserId()));
        entity.setUser(UserPlayerMapper.toEntity(model.getUserPlayer()));
        entity.setPosition(model.getPosition());
        entity.setDorsalNumber(model.getDorsalNumber());
        entity.setStatus(model.getStatus());
        return entity;
    }

    public static PlayerEntity toEntity(ComponentPlayer model) {
        if (model == null) return null;
        PlayerEntity entity = new PlayerEntity();
        entity.setId(UUID.fromString(model.getUserId()));
        entity.setUser(UserPlayerMapper.toEntity(model.getUserPlayer()));
        entity.setPosition(model.getPosition());
        entity.setDorsalNumber(model.getDorsalNumber());
        entity.setStatus(model.getStatus());
        return entity;
    }
}
