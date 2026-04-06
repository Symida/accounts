package com.symida.service.impl;

import com.symida.payload.UserMapper;
import com.symida.payload.request.UserCreateRequest;
import com.symida.payload.response.UserResponse;
import com.symida.repository.UserRepository;
import com.symida.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Override
	public Optional<UserResponse> findByUsername(String username) {
		return userRepository.findByUsername(username)
				.map(userMapper::entityToResponse);
	}

	@Override
	public Optional<UserResponse> createUser(UserCreateRequest request) {
		return Optional.of(request)
				.filter(r -> userRepository.existsByUsernameOrEmail(r.username(), r.email()))
				.map(userMapper::requestToEntity)
				.map(userRepository::save)
				.map(userMapper::entityToResponse);
	}

}

