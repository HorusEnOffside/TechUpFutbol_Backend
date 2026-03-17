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

        UserPlayer userPlayer = new UserPlayer(
            id,
            dto.getName(),
            dto.getMail(),
            dto.getDateOfBirth(),
            dto.getGender(),
            password
        );

        return new Player(userPlayer, dto.getPosition(), dto.getDorsalNumber());
    }

    public static Player toPlayer(PlayerDTO dto, String id, BufferedImage profilePicture) {
        return toPlayer(dto, id, profilePicture, dto.getPassword());
    }

    public static Player toPlayer(PlayerDTO dto, String id, BufferedImage profilePicture, String password) {

        UserPlayer userPlayer = new UserPlayer(
            id,
            dto.getName(),
            dto.getMail(),
            profilePicture,
            dto.getDateOfBirth(),
            dto.getGender(),
            password
        );

        return new Player(userPlayer, dto.getPosition(), dto.getDorsalNumber());
    }

    public static PlayerDTO toDTO(Player player) {
        if (player == null) {
            throw new InvalidInputException("Player no puede ser null");
        }
        return new PlayerDTO(player.getName(), player.getMail(), player.getDateOfBirth(), player.getGender(), player.getPassword(), player.getDorsalNumber(), player.getPosition());
    }
}
