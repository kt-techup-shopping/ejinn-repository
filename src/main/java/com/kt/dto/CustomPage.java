package com.kt.dto;

import java.util.List;

import com.kt.domain.User;

// paggin의 구조
// BE: 한 화면에 몇개 -> LIMIT, 몇 번째 페이지를 보고있나 -> offset (몇개를 건너 뛸 것인가) => (pageNum-1 * limit)
// FE: 한 화면에 몇개 -> limit, 몇개의 페이지가 생기는지(필수는 아님) -> ceil, 총 데이터의 개수 -> count, 데이터들
public record CustomPage (
	List<User> users,
	int size,
	int page,
	int pages,
	long totalElements
){
}

