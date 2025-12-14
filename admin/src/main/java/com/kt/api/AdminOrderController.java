package com.kt.api;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.ApiResult;
import com.kt.common.Paging;
import com.kt.dto.order.OrderResponse;
import com.kt.repository.order.OrderRepository;
import com.kt.repository.order.OrderRepositoryCustom;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {
	private final OrderRepositoryCustom orderRepository;

	// 1. 레포지토리 주입 바로 받아서 할거냐 -> 싱크홀 안티패턴
	// 2. 그래도 서비스를 통해야한다.
	@GetMapping
	public ApiResult<Page<OrderResponse.Search>> search(
		@RequestParam(required = false) String keyword,
		@Parameter(hidden = true) Paging paging
	) {
		return ApiResult.ok(orderRepository.search(keyword, paging.toPageable()));
	}
}
