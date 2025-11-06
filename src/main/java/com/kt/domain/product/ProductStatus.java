package com.kt.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
	SOLD_OUT("품절"),
	ACTIVE("판매중"),
	INACTIVE("판매중지");

	private final String description;
}
