package com.escuela.techcup.core.model;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Gender;

import lombok.Data;

@Data
public class Student extends UserPlayer {
    private int semester;

    public Student(String id, String name, String email, LocalDate dateOfBirth, Gender gender, int semester, String password) {
        super(id, name, email, dateOfBirth, gender, password);
        this.semester = semester;
    }

    public Student(String id, String name, String email, LocalDate dateOfBirth, Gender gender, int semester, String password, BufferedImage profilePicture) {
        super(id, name, email, dateOfBirth, gender, password, profilePicture);
        this.semester = semester;
    }

}
