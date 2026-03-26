package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;

public class PlayerMapper {

    private PlayerMapper() {
    }
    
    public static Player toModel(PlayerEntity entity) {
        if (entity == null) return null;
        UserPlayer userPlayer = UserPlayerMapper.toModel(entity.getUser());
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
        entity.setId(model.getUserId());
        entity.setUser(UserPlayerMapper.toEntity(model.getUserPlayer()));
        entity.setPosition(model.getPosition());
        entity.setDorsalNumber(model.getDorsalNumber());
        entity.setStatus(model.getStatus());
        return entity;
    }
}
