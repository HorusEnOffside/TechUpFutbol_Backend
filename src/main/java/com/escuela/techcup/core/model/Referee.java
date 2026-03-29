package com.escuela.techcup.core.model;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;

public class Referee extends User implements RefereeActions { //arbitro
    
    public Referee( String id, String name, String mail, LocalDate dateOfBirth, Gender gender, String password) {
        super(id, name, mail, dateOfBirth, gender, password);
        roles.add(UserRole.REFEREE);
    }

    public Referee( String id, String name, String mail, LocalDate dateOfBirth, Gender gender, String password, BufferedImage profilePicture) {
        super(id, name, mail, dateOfBirth, gender, password, profilePicture);
        roles.add(UserRole.REFEREE);
    }
}
