package com.symida.accounts.service.impl;

import com.symida.accounts.entity.Account;
import com.symida.accounts.repository.AccountRepository;
import com.symida.accounts.service.AccountService;
import lombok.RequiredArgsConstructor;
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

    @Override
    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public boolean existsByUsernameOrEmail(String username, String email) {
        return accountRepository.existsByUsernameOrEmail(username, email);
    }

    @Override
    public Account save(Account account) {
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
    public CompletableFuture<Account> save(String username, String password, String email) {
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

