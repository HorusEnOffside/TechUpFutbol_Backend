package com.escuela.techcup.core.model;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Gender;

import lombok.Data;

@Data
public abstract class User {
    protected String id;
    protected String name;
    protected String mail;
    protected BufferedImage profilePicture;
    protected LocalDate dateOfBirth;
    protected Gender gender;
    protected String password;

    protected User( String id, String name, String mail, LocalDate dateOfBirth, Gender gender, String password) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.password = password;
    }

    protected User( String id, String name, String mail, BufferedImage profilePicture, LocalDate dateOfBirth, Gender gender, String password) {
        this(id, name, mail, dateOfBirth, gender, password);
        this.profilePicture = profilePicture;
    }
    
}
