package com.kt.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kt.domain.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	// 1. 네이티브쿼리로 작성
	// 2. jqpl로 작성
	// 3. 쿼리메소드로 작성
	// 4. 조회할 때는 동적쿼리를 작성하게 해줄 수 있는 querydsl
}
