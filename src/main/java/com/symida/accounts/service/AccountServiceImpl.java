package com.symida.accounts.service;

import com.symida.accounts.entity.Account;
import com.symida.accounts.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;

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
        var account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setEmail(email);
        return CompletableFuture.completedFuture(accountRepository.save(account));
    }

    @Override
    public CompletableFuture<Void> changePassword(UUID accountId, String password) {
        var account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new IllegalArgumentException("Account not found");
        }
        account.get().setPassword(password);
        return CompletableFuture.completedFuture(null);
    }
}
