package com.symida.accounts.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserInfoResponse {

    private UUID id;
    private String username;
    private String email;
}