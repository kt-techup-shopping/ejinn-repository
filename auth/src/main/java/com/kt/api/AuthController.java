package com.kt.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.ApiResult;
import com.kt.dto.LoginRequest;
import com.kt.dto.LoginResponse;
import com.kt.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/login")
	public ApiResult<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
		var pair = authService.login(request.loginId(), request.password());

		return ApiResult.ok(new LoginResponse(pair.getFirst(), pair.getSecond()));
	}
	// 인증방식
	// 1. 세션기반 인증 -> 서버쪽에 작은 공간에 사용자 정보를 저장 - 만료 시간
	// -> 서버에서 관리하기 때문에 보안성이 좋음
	// -> A서버에서 인가를 해줌, B서버에서는 인가된 정보가 없음
	// -> 세션클러스터링: redis 등 외부 저장소를 통해서 단일 세션
	// -> 스티키세션: 세션이 A서버에서 생성되었으면 A서버로 트래픽 고정
	// 2. 토큰기반 인증 (JWT) -> 사용자가 토큰을 가지고 있다가 요청할 때마다 같이 줌 ->
	// 단점: 매번 검사를 해야함
	// 장점: 서버에서 관리하지 않기 때문에 부하가 적음, 분산환경에 유리
	// 3 OAuth2.0 기반 인증 -> 내 서버에서 하는게 아니라 탐한테 맡기는 방식 (구글, 카카오, 네이버 등)
	// 장점: 서버 개발자들 편하려고 씀 -> 개인정보를 취급하지 않아도 되서

}
