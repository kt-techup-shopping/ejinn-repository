package com.kt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kt.common.CustomException;
import com.kt.common.ErrorCode;
import com.kt.domain.user.User;

// <T, ID>
// T: Entity 클래스
// ID: Entity 클래스의 PK 타입
public interface UserRepository extends JpaRepository<User, Long> {
	// JPA 쿼리작성 방법
	// 1. native query
	// 2. jpql
	// 3. querymethod

	Boolean existsByLoginId(String loginId);

	Page<User> findAllByNameContaining(String name, Pageable pageable);

	default User findByIdOrThrow(Long id, ErrorCode errorCode) {
		return findById(id).orElseThrow(() -> new CustomException(errorCode));
	}
}
