package com.escuela.techcup.controller.dto;

import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.Position;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentPlayerDTO extends StudentUserDTO {

    @Min(value = 1, message = "El dorsal debe ser mayor a 0")
	private int dorsalNumber;

	@NotNull(message = "position es obligatorio")
	private Position position;

    public StudentPlayerDTO(String name, String mail, LocalDate dateOfBirth, Gender gender, String password, Integer semester, int dorsalNumber, Position position) {
        super(name, mail, dateOfBirth, gender, password, semester);
		this.dorsalNumber = dorsalNumber;
		this.position = position;
	}
    
}
