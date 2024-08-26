package com.symida.accounts.service.impl;

import com.symida.accounts.entity.Authority;
import com.symida.accounts.entity.AuthorityName;
import com.symida.accounts.repository.AuthorityRepository;
import com.symida.accounts.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authorityRepository;

    @Override
    public Optional<Authority> findByName(AuthorityName name) {
        return authorityRepository.findByName(name);
    }
}
