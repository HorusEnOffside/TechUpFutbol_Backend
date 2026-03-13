package com.escuela.techcup.controller.dto;

import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerType;
import com.escuela.techcup.core.model.enums.Position;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

@Data
public class PlayerDTO {
	@NotBlank(message = "name es obligatorio")
	private String name;

	@NotBlank(message = "email es obligatorio")
	@Email(message = "email no es valido")
	private String email;

	@NotNull(message = "dateOfBirth es obligatorio")
	@Past(message = "dateOfBirth debe ser una fecha pasada")
	private LocalDate dateOfBirth;

	@NotNull(message = "gender es obligatorio")
	private Gender gender;

	@NotNull(message = "playerType es obligatorio")
	private PlayerType playerType;

	@Min(value = 1, message = "El dorsal debe ser mayor a 0")
	private int dorsalNumber;

	@NotNull(message = "position es obligatorio")
	private Position position;

	@NotBlank(message = "password es obligatorio")
	private String password;
}
