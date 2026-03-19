package com.escuela.techcup.controller.dto;
import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "name es obligatorio")
	private String name;

	@NotBlank(message = "mail es obligatorio")
	@Email(message = "mail no es valido")
	private String mail;

	@NotNull(message = "dateOfBirth es obligatorio")
	@Past(message = "dateOfBirth debe ser una fecha pasada")
	private LocalDate dateOfBirth;

	@NotNull(message = "gender es obligatorio")
	private Gender gender;

	@NotBlank(message = "password es obligatorio")
	private String password;

	public UserDTO(String name, String mail, LocalDate dateOfBirth, Gender gender, String password) {
		this.name = name;
		this.mail = mail;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.password = password;
	}
}