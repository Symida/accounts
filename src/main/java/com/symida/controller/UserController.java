package com.symida.controller;

import com.symida.payload.request.UserCreateRequest;
import com.symida.payload.response.UserResponse;
import com.symida.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;


	@GetMapping
	public ResponseEntity<UserResponse> getUserByUsername(@RequestParam String username) {
		return userService.findByUsername(username)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest request) {
		return userService.createUser(request)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.badRequest().build());
	}
}
