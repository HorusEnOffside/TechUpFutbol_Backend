package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

	private String token;
	private String id;
	private String mail;
	private Set<UserRole> roles;
}
