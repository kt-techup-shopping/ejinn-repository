package com.kt.common;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@ApiResponses(value = {
	@ApiResponse(responseCode = "400", description = "검증 실패죠"),
	@ApiResponse(responseCode = "500", description = "서버 에러에요")
})
public abstract class SwaggerAssistance {
}
