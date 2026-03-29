package com.escuela.techcup.core.model;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Gender;

public class Organizer extends User implements OrganizerActions {

    public Organizer( String id, String name, String mail, LocalDate dateOfBirth, Gender gender, String password) {
        super(id, name, mail, dateOfBirth, gender, password);
    }

    public Organizer( String id, String name, String mail, LocalDate dateOfBirth, Gender gender, String password, BufferedImage profilePicture) {
        super(id, name, mail, dateOfBirth, gender, password, profilePicture);
    }
}
