package com.symida.service;

import com.symida.payload.request.UserCreateRequest;
import com.symida.payload.response.UserResponse;
import jakarta.validation.Valid;

import java.util.Optional;

public interface UserService {

	Optional<UserResponse> findByUsername(String username);

	Optional<UserResponse> createUser(@Valid UserCreateRequest request);

}
