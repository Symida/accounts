package com.symida.accounts.service.impl;

import com.symida.accounts.entity.Account;
import com.symida.accounts.repository.AccountRepository;
import com.symida.accounts.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Account login(Account account) {
        Optional<Account> foundAccount = Optional.empty();
        if (account.getEmail() != null) {
            foundAccount = accountRepository.findByEmail(account.getEmail());
        }
        if (account.getUsername() != null) {
            foundAccount = accountRepository.findByUsername(account.getUsername());
        }
        if (foundAccount.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }
        if (!passwordEncoder.matches(account.getPassword(), foundAccount.get().getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }
        return foundAccount.get();
    }

    @Override
    public Account register(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Override
    public CompletableFuture<Account> getAccountByUsername(String username) {
        return CompletableFuture.completedFuture(accountRepository.findByUsername(username).orElse(null));
    }

    @Override
    public CompletableFuture<Account> getAccount(UUID id) {
        return CompletableFuture.completedFuture(accountRepository.findById(id).orElse(null));
    }

    @Override
    public CompletableFuture<Collection<Account>> getAllAccounts(Set<UUID> ids) {
        return CompletableFuture.completedFuture(accountRepository.findAllById(ids));
    }

    @Override
    public CompletableFuture<Collection<Account>> getAllAccounts() {
        return CompletableFuture.completedFuture(accountRepository.findAll());
    }

    @Override
    public CompletableFuture<Account> register(String username, String password, String email) {
        return CompletableFuture.completedFuture(
                accountRepository.save(Account.builder()
                        .username(username)
                        .password(password)
                        .email(email)
                        .build()
                ));
    }

    @Override
    public CompletableFuture<Void> changePassword(UUID accountId, String password) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new IllegalArgumentException("Account not found");
        }
        account.get().setPassword(password);
        return CompletableFuture.completedFuture(null);
    }
}
