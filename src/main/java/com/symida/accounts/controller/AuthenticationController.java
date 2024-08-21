package com.symida.accounts.controller;

import com.symida.accounts.configuration.jwt.JwtUtils;
import com.symida.accounts.entity.Account;
import com.symida.accounts.entity.Authority;
import com.symida.accounts.entity.AuthorityEnum;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final AccountService accountService;

    private final AuthorityService authorityService;

    @GetMapping
    public String index() {
        return "index";
    }

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
//    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//        return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
//    }
//
//    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//        return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
//    }

        Account account = Account.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .build();

        Set<String> strRoles = signUpRequest.getRole();
        Set<Authority> authorities = new HashSet<>();

        //var checker = AuthorityEnum.ADMIN.name();
        if (strRoles == null) {
            Authority authority = authorityService.findByAuthority(AuthorityEnum.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            authorities.add(authority);
        } else {
            strRoles.forEach(role -> {
                Authority authority;
                if (role.equals("admin")) {
                    authority = authorityService.findByAuthority(AuthorityEnum.ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                } else {
                    authority = authorityService.findByAuthority(AuthorityEnum.USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                }
                authorities.add(authority);
            });
        }

        account.setAuthorities(authorities);
        accountService.register(account);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }


}

//@PostMapping("/login")
//public ResponseEntity<Account> login(@RequestBody Account account) {
//
//    Account foundAccount = accountService.login(account);
//    return new ResponseEntity<>(
//            foundAccount,
//            HttpStatus.OK
//    );
//}
//
//@PostMapping("/register")
//public ResponseEntity<Account> register(@RequestBody Account account) {
//    Account savedAccount = accountService.register(account);
//    return new ResponseEntity<>(
//            savedAccount,
//            HttpStatus.CREATED
//    );
//}
