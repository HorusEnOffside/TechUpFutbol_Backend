package com.escuela.techcup.core.model;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerType;

import lombok.Data;

@Data
public class Student extends UserPlayer {
    private int semester;

    public Student(String id, String name, String email, LocalDate dateOfBirth, Gender gender, int semester, String password) {
        super(id, name, email, dateOfBirth, gender, PlayerType.ESTUDIANTE, password);
        this.semester = semester;
    }

    public Student(String id, String name, String email, BufferedImage profilePicture, LocalDate dateOfBirth, Gender gender, int semester, String password) {
        super(id, name, email, profilePicture, dateOfBirth, gender, PlayerType.ESTUDIANTE, password);
        this.semester = semester;
    }

}
