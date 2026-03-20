package com.escuela.techcup.controller.dto;

import java.time.LocalDate;
import java.util.Set;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.core.model.enums.UserRole;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PlayerResponseDTO extends UserResponseDTO {

    private int dorsalNumber;
    private Position position;

    public PlayerResponseDTO(String name, String mail, LocalDate dateOfBirth, Gender gender, int dorsalNumber, Position position, Set<UserRole> roles) {
        super(name, mail, dateOfBirth, gender, roles);
        this.dorsalNumber = dorsalNumber;
        this.position = position;
    }
}