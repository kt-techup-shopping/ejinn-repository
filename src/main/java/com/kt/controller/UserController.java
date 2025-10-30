package com.kt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kt.dto.UserCreateRequest;
import com.kt.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	// DI
	// 생성자 주입 씀
	// 이유는... 재할당 금지 (불변성 유지)

	private final UserService userService;

	@PostMapping("/users")
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestBody UserCreateRequest request) {
		// jackson object mapper -> json 과 dto 매핑

		userService.create(request);
	}

	// @ReqeustBody 어노테이션 안붙히면
	// var objectMapper = new ObjectMapper()
	// var dto: objectMapper.readValue(request, UserCreateRequest.class)
	// ....
}
