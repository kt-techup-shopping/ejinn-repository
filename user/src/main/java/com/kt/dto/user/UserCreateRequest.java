package com.kt.dto.user;

import java.time.LocalDate;

import com.kt.domain.user.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// bean validation으로 유효성 검사
public record UserCreateRequest(
	@NotBlank
	String loginId,
	@NotBlank
	// 최소 8자 등등 -> 정규식
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^])[A-Za-z\\d!@#$%^]{8,}$")
	String password,
	@NotBlank
	String name,
	@NotBlank
	String email,
	@NotBlank
	@Pattern(regexp = "^(0\\d{1,2})-(\\d{3,4})-(\\d{4})$")
	String mobile,
	@NotNull
	Gender gender,
	@NotNull // yyyy-mm-dd
	LocalDate birthday
) {
}
