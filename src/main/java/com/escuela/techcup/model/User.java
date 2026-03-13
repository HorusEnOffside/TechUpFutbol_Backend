package com.escuela.techcup.model;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import com.escuela.techcup.model.enums.Gender;

import lombok.Data;

@Data
public abstract class User {
    protected String id;
    protected String name;
    protected String email;
    protected BufferedImage profilePicture;
    protected LocalDate dateOfBirth;
    protected Gender gender;

    protected User( String id, String name, String email, LocalDate dateOfBirth, Gender gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    protected User( String id, String name, String email, BufferedImage profilePicture, LocalDate dateOfBirth, Gender gender) {
        this(id, name, email, dateOfBirth, gender);
        this.profilePicture = profilePicture;
    }
    
}
