package com.escuela.techcup.controller.dto;

import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Gender;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDTO {
    private String name;
    private String mail;
    private LocalDate dateOfBirth;
    private Gender gender;

    public UserResponseDTO(String name, String mail, LocalDate dateOfBirth, Gender gender) {
        this.name = name;
        this.mail = mail;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }
}