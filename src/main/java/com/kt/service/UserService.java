package com.kt.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.kt.domain.User;
import com.kt.dto.CustomPage;
import com.kt.dto.UserCreateRequest;
import com.kt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public void create(UserCreateRequest request) {
		var newUser = new User(
			userRepository.selectMaxId() + 1,
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
		return userRepository.existByLoginId(loginId);
	}

	public void changePassword(Long id, String oldPassword, String password) {
		var user = userRepository
			.selectById(id)
			.orElseThrow(() -> new IllegalArgumentException("없어요"));

		if (!oldPassword.equals(user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 틀림");
		}

		if (oldPassword.equals(password)) {
			throw new IllegalArgumentException("기존 비밀번호와 동일");
		}

		userRepository.updatePassword(id, password);
	}

	public CustomPage search(int page, int size, String keyword) {
		var pair = userRepository.selectAll(page - 1, size, keyword);
		var pages = (int)Math.ceil((double) pair.getSecond() / size);

		return new CustomPage(
			pair.getFirst(),
			size,
			page,
			pages,
			pair.getSecond()
		);
	}

	public User detail(Long id) {
		return userRepository
			.selectById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디"));
	}

	public void update(Long id, String name, String email, String mobile){
		userRepository
			.selectById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디"));

		userRepository.updateById(id, name, email, mobile);
	}

	public void delete(Long id) {
		userRepository
			.selectById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디"));

		userRepository.deleteById(id);
	}

	public void initPassword(Long id) {
		userRepository
			.selectById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디"));

		String encoded = "abcdabcd1234!@#$";

		userRepository.initPassword(id, encoded);
	}
}
