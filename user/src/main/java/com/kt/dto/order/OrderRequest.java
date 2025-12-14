package com.kt.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface OrderRequest {
	record Create(
		@NotNull
		Long productId,
		@NotNull
		@Min(1)
		Long Quantity,
		@NotBlank
		String receiverName,
		@NotBlank
		String receiverAddress,
		@NotBlank
		String receiverMobile
	) {
	}
}
