package com.kt.security;

public interface CurrentUser {
	// jwt 파싱해서 넣어줌
	Long getId();

	String getLoginId();
}
