package com.symida.accounts.controller;

import com.symida.accounts.configuration.jwt.JwtUtils;
import com.symida.accounts.entity.Account;
import com.symida.accounts.entity.Authority;
import com.symida.accounts.entity.AuthorityName;
import com.symida.accounts.payload.request.LoginRequest;
import com.symida.accounts.payload.request.RegisterRequest;
import com.symida.accounts.payload.response.MessageResponse;
import com.symida.accounts.payload.response.UserInfoResponse;
import com.symida.accounts.service.AccountService;
import com.symida.accounts.service.AuthorityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AccountService accountService;
    private final AuthorityService authorityService;

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

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(account);

        List<String> roles = account.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(
                        new UserInfoResponse(
                                account.getId(),
                                account.getUsername(),
                                account.getEmail(),
                                roles
                        )
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
                .password(signUpRequest.getPassword())
                .build();

        Set<String> strRoles = signUpRequest.getRole();
        Set<Authority> authorities = new HashSet<>();

        if (strRoles == null) {
            Authority authority = authorityService.findByName(AuthorityName.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            authorities.add(authority);
        } else {
            strRoles.forEach(role -> {
                Authority authority;
                if (role.equals("admin")) {
                    authority = authorityService.findByName(AuthorityName.ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                } else {
                    authority = authorityService.findByName(AuthorityName.USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                }
                authorities.add(authority);
            });
        }

        account.setAuthorities(authorities);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}
