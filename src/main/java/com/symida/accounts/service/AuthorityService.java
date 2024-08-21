package com.symida.accounts.service;

import com.symida.accounts.entity.Authority;
import com.symida.accounts.entity.AuthorityEnum;

import java.util.Optional;

public interface AuthorityService {
    Optional<Authority> findByAuthority(AuthorityEnum authority);
}
