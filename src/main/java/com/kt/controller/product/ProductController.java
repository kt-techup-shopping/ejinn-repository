package com.kt.controller.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kt.dto.product.ProductRequest;
import com.kt.service.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Product")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestBody @Valid ProductRequest.Create request) {
		productService.create(
			request.getName(),
			request.getPrice(),
			request.getQuantity()
		);
	}

	@PutMapping("/{id}")
	public void update(
		@PathVariable Long id,
		@RequestBody @Valid ProductRequest.Update request
	) {
		productService.update(
			id,
			request.getName(),
			request.getPrice(),
			request.getQuantity()
		);
	}

	@PatchMapping("/{id}/sold-out")
	public void soldOut(@PathVariable Long id) {
		productService.soldOut(id);
	}

	@PatchMapping("/{id}/activate")
	public void activate(@PathVariable Long id) {
		productService.soldOut(id);
	}

	@PatchMapping("/{id}/in-activate")
	public void inActivate(@PathVariable Long id) {
		productService.inActivate(id);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		productService.delete(id);
	}
}
