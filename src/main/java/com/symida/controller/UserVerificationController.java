package com.symida.controller;

import com.symida.payload.request.UserVerifyRequest;
import com.symida.payload.response.UserResponse;
import com.symida.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Hidden
@RequestMapping("/internal/v1/auth")
@RequiredArgsConstructor
public class UserVerificationController {

	private final UserService userService;

	@PostMapping("/verify")
	public ResponseEntity<UserResponse> verifyUser(@RequestBody UserVerifyRequest request) {
		return userService.verifyUser(request)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.badRequest().build());
	}
}
