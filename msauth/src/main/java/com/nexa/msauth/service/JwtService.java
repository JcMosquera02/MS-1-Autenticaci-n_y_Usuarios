package com.nexa.msauth.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

	@Value("${ms1.jwt.secret}")
	private String secret;

	@Value("${ms1.jwt.expiration:86400000}")
	private long expiration;

	private SecretKey getSigningKey() {
		byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generarToken(Long id, String correo, String rol) {
		return Jwts.builder()
				.subject(correo)
				.claims(Map.of("id", id, "rol", rol))
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSigningKey())
				.compact();
	}

	public boolean validarToken(String token) {
		try {
			Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public String getCorreo(String token) {
		return Jwts.parser().verifyWith(getSigningKey()).build()
				.parseSignedClaims(token).getPayload().getSubject();
	}

	public Long getId(String token) {
		return Jwts.parser().verifyWith(getSigningKey()).build()
				.parseSignedClaims(token).getPayload().get("id", Long.class);
	}

	public String getRol(String token) {
		return Jwts.parser().verifyWith(getSigningKey()).build()
				.parseSignedClaims(token).getPayload().get("rol", String.class);
	}
}
