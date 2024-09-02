package com.symida.accounts.service;

import com.symida.accounts.entity.Account;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface AccountService {

    Optional<Account> findByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

    Account save(Account account);

    CompletableFuture<Account> getAccountByUsername(String username);

    CompletableFuture<Account> getAccount(UUID id);

    CompletableFuture<Collection<Account>> getAllAccounts(Set<UUID> ids);

    CompletableFuture<Collection<Account>> getAllAccounts();

    CompletableFuture<Account> save(String username, String password, String email);

    CompletableFuture<Void> changePassword(UUID accountId, String password);

}
