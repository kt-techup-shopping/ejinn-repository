package com.kt.common;

import org.springframework.data.domain.PageRequest;

public record Paging (
	int page,
	int size
	// TODO: 정렬
){
	public PageRequest toPageable() {
		return PageRequest.of(page - 1, size);
	}
}
