package com.kt.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.ApiResult;
import com.kt.common.SwaggerAssistance;
import com.kt.dto.user.UserCreateRequest;
import com.kt.dto.user.UserUpdatePasswordRequest;
import com.kt.service.UserService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "User", description = "user api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController extends SwaggerAssistance {
	// DI
	// 생성자 주입 씀
	// 이유는... 재할당 금지 (불변성 유지)

	private final UserService userService;

	// API 문서화에는 2가지가 있음
	// 1. Swagger
	// 장점: UI 이쁨, 어노테이션으로 작성
	// 단점: 프로덕션 코드에 Swagger 관련 어노테이션이 있음 -> SRP 위반 (컨트롤러가 이거까지해서), 코드가 더러움
	// 2. ResrDocs
	// 장점: 프로덕션 코드에 작성 안함
	// 단점: UI 구림, 문서작성에 시간이 걸림 (테스트 코드 기반)
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResult<Void> create(@Valid @RequestBody UserCreateRequest request) {
		// jackson object mapper -> json과 dto 매핑

		userService.create(request);
		return ApiResult.ok();
	}

	// @ReqeustBody 어노테이션 안붙히면
	// var objectMapper = new ObjectMapper()
	// var dto: objectMapper.readValue(request, UserCreateRequest.class)
	// ....

	@GetMapping("/duplicate-login-id")
	@ResponseStatus(HttpStatus.OK)
	// @RequestParam -> queryString에 loginId 있으면 매핑해줌, 디폴트는 required = true
	// IllgerArgumentException 발생 시 에러
	public ApiResult<Boolean> isDuplicateLoginId(@RequestParam String loginId) {
		var result = userService.isDuplicateLoginId(loginId);

		return ApiResult.ok(result);
	}

	// uri는 식별이 가능해야한다
	// body에 json으로 전달

	// 1. 바디에 id 값
	// 2. uri에 id 값
	// 3. 인증/인가 객체에서 id 값
	@PutMapping("/{id}/change-password")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> changePassword(
		@PathVariable Long id,
		@RequestBody @Valid UserUpdatePasswordRequest request
	) {
		userService.changePassword(id, request.oldPasswrod(), request.newPassword());

		return ApiResult.ok();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> delete(@PathVariable Long id) {
		userService.delete(id);

		return ApiResult.ok();
	}
}
