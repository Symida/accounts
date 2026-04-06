package com.symida.payload.request;

import jakarta.validation.constraints.NotBlank;

public record UserVerifyRequest(
		@NotBlank String username,
		@NotBlank String password
) {
}