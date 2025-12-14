package com.kt.repository.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kt.common.CustomException;
import com.kt.common.ErrorCode;
import com.kt.domain.user.User;

import jakarta.validation.constraints.NotNull;

// <T, ID>
// T: Entity 클래스
// ID: Entity 클래스의 PK 타입
public interface UserRepository extends JpaRepository<User, Long> {
	// JPA 쿼리작성 방법
	// 1. native query
	// 2. jpql
	// 3. querymethod

	Boolean existsByLoginId(String loginId);

	Optional<User> findByLoginId(String loginId);

	Page<User> findAllByNameContaining(String name, Pageable pageable);

	default User findByIdOrThrow(Long id, ErrorCode errorCode) {
		return findById(id).orElseThrow(() -> new CustomException(errorCode));
	}

	// N + 1 문제
	// Collection, stream, foreach에서 발생할 수 있음

	// 	N + 1 문제 해결 방법
	//  1. fetch join 사용 → JPQL전용 → 딱 1번 사용 2번 사용하면 에러남
	//  2. @EntityGraph 사용 → JPA 표준 기능 → 여러번 사용가능
	//  3. batch fetch size 옵션 사용 → 전역 설절 → paging 동작원리와 같아서 성능 이슈 있을 수 있음
	//  4. @BatchSize 어노테이션 사용 → 특정 엔티티에만 사용
	//  5. native query로 사용

	// JPQL로 작성
	// @Query("""
	// 	SELECT DISTINCT u FROM user u
	// 	LEFT JOIN FETCH u.orders o
	// 	LEFT JOIN FETCH o.orderProducts op // FETCH를 2번은 못씀
	// 	WHERE u.id = :id
	// 	""")
	// @NotNull
	// Optional<User> findById(@NotNull Long id);

	@EntityGraph(attributePaths = "orders")
	@NotNull
	Optional<User> findById(@NotNull Long id);
}
