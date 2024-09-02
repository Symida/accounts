package com.symida.accounts.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class AccountInfoResponse {

    private UUID id;

    private String email;

    private String username;

    private String role;

    private String password;
}