package com.escuela.techcup.service.impl;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.escuela.techcup.model.Player;
import com.escuela.techcup.model.UserPlayer;
import com.escuela.techcup.model.enums.Gender;
import com.escuela.techcup.model.enums.PlayerType;
import com.escuela.techcup.model.enums.Position;
import com.escuela.techcup.service.PlayerService;
import com.escuela.techcup.valid.PlayerValidator;

@Service
public class PlayerServiceImpl implements PlayerService {


    @Override
    public Player createSportsProfile(String id, String name, String email, LocalDate dateOfBirth, Gender gender, PlayerType playerType, int dorsalNumber, Position position) {
        PlayerValidator.validateInput(id, name, email, dorsalNumber);

        UserPlayer userPlayer = new UserPlayer(id, name, email, dateOfBirth, gender, playerType);
        
        return new Player(userPlayer, position, dorsalNumber);
    }

    @Override
    public Player createSportsProfile(String id, String name, String email, LocalDate dateOfBirth, Gender gender, PlayerType playerType, int dorsalNumber, Position position, BufferedImage profilePicture) {
        PlayerValidator.validateInput(id, name, email, dorsalNumber);

        UserPlayer userPlayer = new UserPlayer(id, name, email, profilePicture, dateOfBirth, gender, playerType);

        return new Player(userPlayer, position, dorsalNumber);
    }

}
