package com.escuela.techcup.core.model;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;

import lombok.Data;

@Data
public abstract class User {
    protected String id;
    protected String name;
    protected String mail;  
    protected LocalDate dateOfBirth;
    protected Gender gender;
    protected String password;
    protected Set<UserRole> roles;
    protected BufferedImage profilePicture;

    protected User( String id, String name, String mail, LocalDate dateOfBirth, Gender gender, String password) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.password = password;
        this.roles = EnumSet.of(UserRole.BASEUSER);
    }

    protected User(String id, String name, String mail, LocalDate dateOfBirth, Gender gender, String password, BufferedImage profilePicture) {
        this(id, name, mail, dateOfBirth, gender, password);
        this.profilePicture = profilePicture;
    }

    public void setPrimaryRole(UserRole role) {
        this.roles = EnumSet.of(role);
    }

    public void addRole(UserRole role) {
        this.roles.add(role);
    }
    
}
