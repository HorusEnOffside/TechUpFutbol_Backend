package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.escuela.techcup.core.exception.InvalidInputException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class JwtServiceImpl implements JwtService {

	private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expiration}")
	private long jwtExpiration;

	@Override
	public String generateToken(String userId, String email, Set<String> roles) {
		if (userId == null || userId.isBlank()) {
			throw new InvalidInputException("userId is required");
		}
		if (email == null || email.isBlank()) {
			throw new InvalidInputException("email is required");
		}
		if (roles == null || roles.isEmpty()) {
			throw new InvalidInputException("At least one role is required");
		}

		long now = System.currentTimeMillis();
		long expirationTime = now + jwtExpiration;

		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

		return Jwts.builder()
			.subject(userId)
			.claim("email", email)
			.claim("roles", roles)
			.issuedAt(new Date(now))
			.expiration(new Date(expirationTime))
			.signWith(key)
			.compact();
	}

	@Override
	public String extractUserId(String token) {
		return extractClaims(token).getSubject();
	}

	@Override
	public String extractEmail(String token) {
		return extractClaims(token).get("email", String.class);
	}

	@Override
	public boolean isTokenValid(String token) {
		if (token == null || token.isBlank()) {
			log.debug("JWT validation failed: token is empty");
			return false;
		}

		try {
			parseToken(token);
			return true;
		} catch (Exception e) {
			log.debug("JWT validation failed: {}", e.getMessage());
			return false;
		}
	}

	private Claims extractClaims(String token) {
		return parseToken(token);
	}

	private Claims parseToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<String> extractRoles(String token) {
		Claims claims = extractClaims(token);
		List<String> roles = (List<String>) claims.get("roles");
		if (roles == null) return Set.of();
		return new java.util.HashSet<>(roles);
	}
}
