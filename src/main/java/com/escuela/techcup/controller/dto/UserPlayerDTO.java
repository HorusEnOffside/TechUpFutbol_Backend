package com.escuela.techcup.controller.dto;

import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Gender;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserPlayerDTO extends UserDTO {

    public UserPlayerDTO(String name, String mail, LocalDate dateOfBirth, Gender gender, String password) {
        super(name, mail, dateOfBirth, gender, password);
    }
}
