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
	@NotBlank(message = "name is required")
	private String name;

	@NotBlank(message = "mail is required")
	@Email(message = "mail is not valid")
	private String mail;

	@NotNull(message = "dateOfBirth is required")
	@Past(message = "dateOfBirth must be a past date")
	private LocalDate dateOfBirth;

	@NotNull(message = "gender is required")
	private Gender gender;

	@NotBlank(message = "password is required")
	private String password;

	public UserDTO(String name, String mail, LocalDate dateOfBirth, Gender gender, String password) {
		this.name = name;
		this.mail = mail;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.password = password;
	}
}