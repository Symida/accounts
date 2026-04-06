package com.symida.payload.response;

import com.symida.entity.Role;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(
		UUID id,
		String username,
		String email,
		Role role
) {
}