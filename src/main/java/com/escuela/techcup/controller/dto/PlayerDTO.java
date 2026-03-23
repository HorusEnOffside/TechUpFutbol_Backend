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
public class PlayerDTO extends UserPlayerDTO {

	@Min(value = 1, message = "Dorsal number must be greater than 0")
	private int dorsalNumber;

	@NotNull(message = "position is required")
	private Position position;

	public PlayerDTO(String name, String mail, LocalDate dateOfBirth, Gender gender, String password, int dorsalNumber, Position position) {
		super(name, mail, dateOfBirth, gender, password);
		this.dorsalNumber = dorsalNumber;
		this.position = position;
	}

}
