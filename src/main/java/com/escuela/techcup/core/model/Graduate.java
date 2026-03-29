package com.escuela.techcup.core.model;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Gender;

public class Graduate extends UserPlayer {

    public Graduate(String id, String name, String email, LocalDate dateOfBirth, Gender gender, String password) {
        super(id, name, email, dateOfBirth, gender, password);
    }

     public Graduate(String id, String name, String email, LocalDate dateOfBirth, Gender gender, String password, BufferedImage profilePicture) {
        super(id, name, email, dateOfBirth, gender, password, profilePicture);
    }
}
