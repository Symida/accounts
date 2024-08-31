package com.symida.accounts.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class UserInfoResponse {

    private UUID id;

    private String username;

    private String email;
}