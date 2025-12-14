package com.kt.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
	SOLD_OUT("품절"),
	ACTIVATED("판매중"),
	IN_ACTIVATED("판매 중지"),
	DELETED("삭제"),
	;

	private final String description;
}
