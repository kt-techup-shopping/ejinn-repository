package com.kt.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.domain.user.User;
import com.kt.dto.user.UserCreateRequest;
import com.kt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
	// private final UserJDBCRepository userJDBCRepository;
	private final UserRepository userRepository;

	// PSA: 환경설정을 살짝 바꿔서 일관된 서비스를 제공하는 것
	public void create(UserCreateRequest request) {
		var newUser = new User(
			request.loginId(),
			request.password(),
			request.name(),
			request.email(),
			request.mobile(),
			request.gender(),
			request.birthday(),
			LocalDateTime.now(),
			LocalDateTime.now()
		);

		userRepository.save(newUser);
	}

	public boolean isDuplicateLoginId(String loginId) {
		return userRepository.existsByLoginId(loginId);
	}

	public void changePassword(Long id, String oldPassword, String password) {
		var user = userRepository
			.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("없어요"));

		if (!oldPassword.equals(user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 틀림");
		}

		if (oldPassword.equals(password)) {
			throw new IllegalArgumentException("기존 비밀번호와 동일");
		}

		user.changePassword(password);
	}

	public Page<User> search(Pageable pageable, String keyword) {
		return userRepository.findAllByNameContaining(keyword, pageable);
	}

	public User detail(Long id) {
		return userRepository
			.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디"));
	}

	public void update(Long id, String name, String email, String mobile){
		var user = userRepository
			.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디"));

		user.update(name, email, mobile);
	}

	public void delete(Long id) {
		userRepository
			.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디"));

		// 삭제에는 softdelete, harddelete가 있음
		userRepository.deleteById(id);
	}

	public void initPassword(Long id) {
		var user = userRepository
			.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디"));

		String encoded = "abcdabcd1234!@#$";

		user.changePassword(encoded);
	}
}
