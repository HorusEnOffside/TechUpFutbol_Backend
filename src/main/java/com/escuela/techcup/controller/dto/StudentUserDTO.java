package com.escuela.techcup.controller.dto;

import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Career;
import com.escuela.techcup.core.model.enums.Gender;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentUserDTO extends UserPlayerDTO {

    @NotNull(message = "semester is required for students")
    @Min(value = 1, message = "semester must be greater than 0")
    private Integer semester;

    @NotNull(message = "career is required")
    private Career career;

    public StudentUserDTO(String name, String mail, LocalDate dateOfBirth, Gender gender, String password, Integer semester, Career career) {
        super(name, mail, dateOfBirth, gender, password);
        this.semester = semester;
        this.career = career;
    }
}