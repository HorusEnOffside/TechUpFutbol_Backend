package com.escuela.techcup.core.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.escuela.techcup.core.service.JwtService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

@Service
public class JwtServiceImpl implements JwtService {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expiration}")
	private long jwtExpiration;

	@Override
	public String generateToken(String userId, String email, Set<String> roles) {
		long now = System.currentTimeMillis();
		long expirationTime = now + jwtExpiration;

		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

		return Jwts.builder()
			.subject(userId)
			.claim("email", email)
			.claim("roles", roles)
			.issuedAt(new Date(now))
			.expiration(new Date(expirationTime))
			.signWith(key, SignatureAlgorithm.HS256)
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
		try {
			parseToken(token);
			return true;
		} catch (Exception e) {
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
}
