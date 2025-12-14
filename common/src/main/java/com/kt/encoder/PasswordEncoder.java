package com.kt.encoder;

public interface PasswordEncoder {
	String encode(CharSequence rawPassword);

	boolean matches(String rawPassword, String encodedPassword);
}