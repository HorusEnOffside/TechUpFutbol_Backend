package com.escuela.techcup.controller.mapper;

import java.awt.image.BufferedImage;

import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;

public class PlayerMapper {

    public static Player toPlayer(PlayerDTO dto, String id) {
        return toPlayer(dto, id, dto.getPassword());
    }

    public static Player toPlayer(PlayerDTO dto, String id, String password) {
        if (dto == null) {
            throw new InvalidInputException("PlayerDTO no puede ser null");
        }

        UserPlayer userPlayer = new UserPlayer(
            id,
            dto.getName(),
            dto.getEmail(),
            dto.getDateOfBirth(),
            dto.getGender(),
            dto.getPlayerType(),
            password
        );

        return new Player(userPlayer, dto.getPosition(), dto.getDorsalNumber());
    }

    public static Player toPlayer(PlayerDTO dto, String id, BufferedImage profilePicture) {
        return toPlayer(dto, id, profilePicture, dto.getPassword());
    }

    public static Player toPlayer(PlayerDTO dto, String id, BufferedImage profilePicture, String password) {
        if (dto == null) {
            throw new InvalidInputException("PlayerDTO no puede ser null");
        }

        UserPlayer userPlayer = new UserPlayer(
            id,
            dto.getName(),
            dto.getEmail(),
            profilePicture,
            dto.getDateOfBirth(),
            dto.getGender(),
            dto.getPlayerType(),
            password
        );

        return new Player(userPlayer, dto.getPosition(), dto.getDorsalNumber());
    }

    public static PlayerDTO toDTO(Player player) {
        if (player == null) {
            throw new InvalidInputException("Player no puede ser null");
        }

        PlayerDTO dto = new PlayerDTO();
        dto.setName(player.getUserPlayer().getName());
        dto.setEmail(player.getUserPlayer().getEmail());
        dto.setDateOfBirth(player.getUserPlayer().getDateOfBirth());
        dto.setGender(player.getUserPlayer().getGender());
        dto.setPlayerType(player.getUserPlayer().getPlayerType());
        dto.setDorsalNumber(player.getDorsalNumber());
        dto.setPosition(player.getPosition());
        return dto;
    }
}
