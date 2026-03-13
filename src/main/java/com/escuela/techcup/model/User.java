package com.escuela.techcup.model;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import com.escuela.techcup.model.enums.Gender;


public abstract class User {
    protected String id;
    protected String name;
    protected String email;
    protected BufferedImage profilePicture;
    protected LocalDate dateOfBirth;
    protected Gender gender;
    




    public int calculateAge() {
        if (dateOfBirth == null) {
            return 0;
        }
        LocalDate today = LocalDate.now();
        return today.getYear() - dateOfBirth.getYear();
    }
}
