package com.escuela.techcup.controller.dto;

import  java.time.LocalDate;

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

    @NotNull(message = "semester es obligatorio para estudiantes")
    @Min(value = 1, message = "semester debe ser mayor a 0")
    private Integer semester;

    public StudentUserDTO(String name, String mail, LocalDate dateOfBirth, Gender gender, String password, Integer semester) {
        super(name, mail, dateOfBirth, gender, password);
        this.semester = semester;
    }
}
