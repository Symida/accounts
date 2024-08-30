package com.symida.accounts.controller;

import com.symida.accounts.configuration.jwt.JwtUtils;
import com.symida.accounts.entity.Account;
import com.symida.accounts.entity.Role;
import com.symida.accounts.payload.request.LoginRequest;
import com.symida.accounts.payload.request.RegisterRequest;
import com.symida.accounts.payload.response.MessageResponse;
import com.symida.accounts.payload.response.UserInfoResponse;
import com.symida.accounts.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(), loginRequest.getPassword()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Account account = (Account) authentication.getPrincipal();

        String jwtHeader = jwtUtils.generateJwtHeader(account);

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwtHeader)
                .body(
                        UserInfoResponse.builder()
                                .id(account.getId())
                                .username(account.getUsername())
                                .email(account.getEmail())
                                .build()
                );
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest signUpRequest) {
        if (accountService.existsByUsernameOrEmail(signUpRequest.getUsername(), signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email or Username is already in use!"));
        }

        Account account = Account.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .role(Role.USER)
                .password(signUpRequest.getPassword())
                .build();

        account.setRole(Role.USER);

        accountService.register(account);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
