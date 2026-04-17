package com.escuela.techcup.controller.dto;

import java.time.LocalDate;
import java.util.Set;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDTO {
    private String name;
    private String mail;
    private LocalDate dateOfBirth;
    private Gender gender;
    private Set<UserRole> roles;

    public UserResponseDTO(String name, String mail, LocalDate dateOfBirth, Gender gender, Set<UserRole> roles) {
        this.name = name;
        this.mail = mail;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.roles = roles;
    }
}