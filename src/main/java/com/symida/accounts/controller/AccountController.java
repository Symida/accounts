package com.symida.accounts.controller;

import com.symida.accounts.repository.AccountRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController("/")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        log.info(accountRepository.findByUsername("admin"));
        return "login";
    }
}
