package com.symida.accounts.service;

import com.symida.accounts.entity.Authority;
import com.symida.accounts.entity.AuthorityName;

import java.util.Optional;

public interface AuthorityService {

    Optional<Authority> findByName(AuthorityName authority);

}
