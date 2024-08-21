package com.symida.accounts.repository;

import com.symida.accounts.entity.Authority;
import com.symida.accounts.entity.AuthorityEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByAuthority(AuthorityEnum authority);
}
