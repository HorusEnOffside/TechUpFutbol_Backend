package com.escuela.techcup.core.model;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Career;
import com.escuela.techcup.core.model.enums.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Student extends UserPlayer {

    private int semester;
    private Career career;

    public Student(String id, String name, String email, LocalDate dateOfBirth, Gender gender, int semester, Career career, String password) {
        super(id, name, email, dateOfBirth, gender, password);
        this.semester = semester;
        this.career = career;
    }

    public Student(String id, String name, String email, LocalDate dateOfBirth, Gender gender, int semester, Career career, String password, BufferedImage profilePicture) {
        super(id, name, email, dateOfBirth, gender, password, profilePicture);
        this.semester = semester;
        this.career = career;
    }
}