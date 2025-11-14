package com.kt.domain.product;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import com.kt.common.CustomException;

class ProductTest {
	// 객체 생성이 잘 되나

	// 제목을 작성하는 2가지 방법
	// 1. DisplayName
	// @Test
	// @DisplayName("객체 생성 테스트")
	// void create() {
	//
	// }

	// 2. 한글로 메서드명
	@Test
	void 상품_생성_성공() {
		var product = new Product(
			"테스트 상품명",
			100_000L,
			10L
		);

		// 객체 생성 확인
		// product의 이름 필드 값이 올바른 값인 지
		// jupiter.core에 있음 (assertThat)
		assertThat(product.getName()).isEqualTo("테스트 상품명");
		assertThat(product.getPrice()).isEqualTo(100_000L);
		assertThat(product.getStock()).isEqualTo(10L);
	}

	@ParameterizedTest
	@NullAndEmptySource
	void 상품_생성_실패__상품명_null_빈값(String name) {
		assertThrowsExactly(CustomException.class, () -> new Product(
			name,
			100_000L,
			10L
		));
	}

	@Test
	void 상품_생성_실패__가격_음수() {
		assertThrowsExactly(CustomException.class, () -> new Product(
			"상품명",
			-1L,
			10L
		));
	}

	@Test
	void 상품_생성_실패__가격_null() {
		assertThrowsExactly(CustomException.class, () -> new Product(
			"상품명",
			null,
			10L
		));
	}

}