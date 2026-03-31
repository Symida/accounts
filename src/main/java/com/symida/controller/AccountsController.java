package com.symida.controller;

import com.symida.entity.Account;
import com.symida.entity.Role;
import com.symida.payload.request.CreateRequest;
import com.symida.payload.response.AccountInfoResponse;
import com.symida.payload.response.MessageResponse;
import com.symida.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
public class AccountsController {

    private final AccountService accountService;


    @GetMapping
    public ResponseEntity<?> getAccountByUsername(@RequestParam String username) {
        Optional<Account> account = accountService.findByUsername(username);
        if (account.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                AccountInfoResponse.builder()
                        .id(account.get().getId())
                        .email(account.get().getEmail())
                        .username(account.get().getUsername())
                        .role(account.get().getRole().name())
                        .password(account.get().getPassword())
                        .build());
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreateRequest createRequest) {
        if (accountService.existsByUsernameOrEmail(createRequest.getUsername(), createRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email or Username is already in use!"));
        }
        Account account = Account.builder()
                .email(createRequest.getEmail())
                .username(createRequest.getUsername())
                .password(createRequest.getPassword())
                .role(Role.USER)
                .build();
        accountService.save(account);
        return ResponseEntity.ok().build();
    }
}
