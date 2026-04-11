package com.escuela.techcup.controller.dto;

import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Career;
import com.escuela.techcup.core.model.enums.Gender;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TeacherUserDTO extends UserPlayerDTO {

    @NotNull(message = "career is required")
    private Career career;

    public TeacherUserDTO(String name, String mail, LocalDate dateOfBirth, Gender gender, String password, Career career) {
        super(name, mail, dateOfBirth, gender, password);
        this.career = career;
    }
}