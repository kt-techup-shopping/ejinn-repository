package com.kt.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtService {
	private final JwtProperties jwtProperties;

	public String issue(Long id, Date expiration) {
		// id 값은 jwt의 식별자같은 개념
		// claims -> jwt안에 들어갈 정보를 Map 형태로 넣음

		return Jwts
			.builder()
			.subject("shopping")
			.issuer("ejinn1")
			.issuedAt(new Date())
			.id(id.toString())
			.expiration(expiration)
			// key를 넣어줘야함
			.signWith(jwtProperties.getSecret())
			.compact();
	}

	public Date getAccessExpiration() {
		return jwtProperties.getAccessTokenExpiration();
	}

	public Date getRefreshExpiration() {
		return jwtProperties.getRefreshTokenExpiration();
	}

	public boolean validation(String token) {
		return Jwts
			.parser()
			.verifyWith(jwtProperties.getSecret())
			.build()
			.isSigned(token);
	}

	public Long parseId(String token) {
		var id = Jwts
			.parser()
			.verifyWith(jwtProperties.getSecret())
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getId();

		return Long.valueOf(id);
	}
}
