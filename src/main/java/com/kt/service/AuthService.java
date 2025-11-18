package com.kt.service;

import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.common.CustomException;
import com.kt.common.ErrorCode;
import com.kt.common.Preconditions;
import com.kt.repository.user.UserRepository;
import com.kt.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public Pair<String, String> login(String loginId, String password) {
		var user = userRepository
			.findByLoginId(loginId)
			.orElseThrow(() -> new CustomException(ErrorCode.FAIL_LOGIN));

		// 비밀번호 일치하는 지 확인
		// Bcrypt로 암호화되어있음 -> 단방향 해시암호화 (복호화 불가) -> 기본 5번 해시 알고리즘 돌림
		// 일치하는 지 확인할 떈 -> 요청 들어온 비번을 알고리즘 또 돌려서 비교

		Preconditions.validate(passwordEncoder.matches(password, user.getPassword()), ErrorCode.FAIL_LOGIN);

		// 로그인 성공 처리 -> JWT 토큰을 발급
		// 엑세스 토큰과 리프레시 토큰을 발급해서 보내줘야함
		// var accessToken = jwtService

		var accessToken = jwtService.issue(user.getId(), jwtService.getAccessExpiration());
		var refreshToken = jwtService.issue(user.getId(), jwtService.getRefreshExpiration());

		return Pair.of(accessToken, refreshToken);

	}
}
