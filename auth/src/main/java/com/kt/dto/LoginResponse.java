package com.kt.dto;

public record LoginResponse(
	String accessToken,
	String refreshToken
) {
}
