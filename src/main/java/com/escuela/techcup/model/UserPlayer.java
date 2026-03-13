package com.escuela.techcup.model;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import com.escuela.techcup.model.enums.Gender;
import com.escuela.techcup.model.enums.PlayerType;

import lombok.Data;

@Data
public class UserPlayer extends User {
    protected PlayerType playerType;
    
    public UserPlayer(String id, String name, String email, LocalDate dateOfBirth, Gender gender, PlayerType playerType) {
        super(id, name, email, dateOfBirth, gender);
        this.playerType = playerType;
        
    }

    public UserPlayer(String id, String name, String email, BufferedImage profilePicture, LocalDate dateOfBirth, Gender gender, PlayerType playerType) {
        super(id, name, email, profilePicture, dateOfBirth, gender);
        this.playerType = playerType;
    }

}
