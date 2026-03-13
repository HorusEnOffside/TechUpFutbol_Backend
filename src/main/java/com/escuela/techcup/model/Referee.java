package com.escuela.techcup.model;

import java.time.LocalDate;

import com.escuela.techcup.model.enums.Gender;

public class Referee extends User { //arbitro
    
    public Referee( String id, String name, String email, LocalDate dateOfBirth, Gender gender, String password) {
        super(id, name, email, dateOfBirth, gender, password);
    }
}
