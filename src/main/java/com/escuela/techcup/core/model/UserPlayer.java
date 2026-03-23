package com.escuela.techcup.core.model;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Gender;

import lombok.Data;

@Data
public class UserPlayer extends User {
    
    public UserPlayer(String id, String name, String email, LocalDate dateOfBirth, Gender gender, String password) {
        super(id, name, email, dateOfBirth, gender, password);
        
    }

    public UserPlayer(String id, String name, String email, BufferedImage profilePicture, LocalDate dateOfBirth, Gender gender, String password) {
        super(id, name, email, profilePicture, dateOfBirth, gender, password);
    }

}
