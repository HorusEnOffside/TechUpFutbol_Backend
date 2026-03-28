package com.escuela.techcup.security.service;

import java.util.Set;


public interface JwtService {


	String generateToken(String userId, String email, Set<String> roles);

	String extractUserId(String token);

	String extractEmail(String token);

	boolean isTokenValid(String token);
}

