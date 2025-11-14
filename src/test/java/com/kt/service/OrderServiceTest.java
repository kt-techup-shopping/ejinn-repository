package com.kt.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kt.domain.product.Product;
import com.kt.domain.user.Gender;
import com.kt.domain.user.Role;
import com.kt.domain.user.User;
import com.kt.repository.order.OrderRepository;
import com.kt.repository.orderproduct.OrderProductRepository;
import com.kt.repository.product.ProductRepository;
import com.kt.repository.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceTest {
	@Autowired
	private OrderService orderService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	// 동시성 제어할 때는 Lock을 걸어서 처리해야함
	// 1. 비관적 락 (Pessimistic Lock)
	// -> DB에서 지원해주는 Lock -> SELECT ... FOR UPDATE -> 한 명이 들어오면 끝날 때까지 기다리셈
	// 단점: 시간이 오래걸리고 데드락 발생할 수 있음
	// 2. 낙관적 락 (Optimistic Lock)
	// -> 버전관리
	// 처음 입장할 때 버전을 조회 -> 작업 끝나고 -> 나갈 때 다시 버전을 조회해서 같으면 재고 차감
	// 3. 분산 락 (Redis)
	//

	@BeforeEach
	void setUp() {
		orderProductRepository.deleteAll(); // 1) 자식 먼저
		orderRepository.deleteAll();        // 2) 부모
		productRepository.deleteAll();      // 3)
		userRepository.deleteAll();         // 4)
		// orderRepository.deleteAll();
		// productRepository.deleteAll();
		// userRepository.deleteAll();
		// orderProductRepository.deleteAll();
	}

	@Test
	void 주문_생성() {
		// given
		var user = userRepository.save(
			new User(
				"testuser",
				"password",
				"Test User",
				"email",
				"010-0000-0000",
				Gender.MALE,
				LocalDate.now(),
				LocalDateTime.now(),
				LocalDateTime.now(),
				Role.USER
			)
		);

		var product = productRepository.save(
			new Product(
				"테스트 상품명",
				100_000L,
				10L
			)
		);

		// when
		orderService.create(
			user.getId(),
			product.getId(),
			"수신자 이름",
			"수신자 주소",
			"010-1111-1111",
			2L
		);

		// then
		var foundedProduct = productRepository.findByIdOrThrow(product.getId());
		var foundedOrder = orderRepository
			.findAll()
			.stream()
			.findFirst();

		assertThat(foundedProduct.getStock()).isEqualTo(8L);
		assertThat(foundedProduct.getOrderProducts()).isNotEmpty();
		assertThat(foundedOrder).isPresent();
	}

	@Test
	void 동시에_100명_주문() throws InterruptedException {
		var userList = new ArrayList<User>();
		var repeatCount = 500;

		for (int i = 0; i < repeatCount; i++) {
			userList.add(
				new User(
					"testuser-" + i,
					"password",
					"Test User-" + i,
					"email-" + i,
					"010-0000-000" + i,
					Gender.MALE,
					LocalDate.now(),
					LocalDateTime.now(),
					LocalDateTime.now(),
					Role.USER
				)
			);
		}

		var users = userRepository.saveAll(userList);

		var product = productRepository.save(
			new Product(
				"테스트 상품명",
				100_000L,
				10L
			)
		);

		productRepository.flush();
		var executorService = Executors.newFixedThreadPool(100);
		var countDownLatch = new CountDownLatch(repeatCount);
		AtomicInteger successCount = new AtomicInteger(0);
		AtomicInteger failureCount = new AtomicInteger(0);

		for (int i = 0; i < repeatCount; i++) {
			int finalI = i;
			executorService.submit(() -> {
				try {
					var targetUser = users.get(finalI);
					orderService.create(
						targetUser.getId(),
						product.getId(),
						targetUser.getName(),
						"수신자 주소-" + finalI,
						"010-1111-11" + finalI,
						1L
					);
					successCount.incrementAndGet();
				} catch (RuntimeException e) {
					e.printStackTrace();
					failureCount.incrementAndGet();
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();
		executorService.shutdown();

		var foundedProduct = productRepository.findByIdOrThrow(product.getId());

		assertThat(successCount.get()).isEqualTo(10);
		assertThat(failureCount.get()).isEqualTo(490);
		assertThat(foundedProduct.getStock()).isEqualTo(0);
		
		System.out.println("성공한 주문 수: " + successCount.get());
		System.out.println("실패한 주문 수: " + failureCount.get());
		System.out.println("남은 재고 수: " + foundedProduct.getStock());
	}
}
