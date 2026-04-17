package com.escuela.techcup.core.model;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Career;
import com.escuela.techcup.core.model.enums.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Graduate extends UserPlayer {

    private Career career;

    public Graduate(String id, String name, String email, LocalDate dateOfBirth, Gender gender, String password, Career career) {
        super(id, name, email, dateOfBirth, gender, password);
        this.career = career;
    }

    public Graduate(String id, String name, String email, LocalDate dateOfBirth, Gender gender, String password, Career career, BufferedImage profilePicture) {
        super(id, name, email, dateOfBirth, gender, password, profilePicture);
        this.career = career;
    }
}