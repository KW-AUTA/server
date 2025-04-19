package com.auta.server.adapter.out.persistence.auth;

import com.auta.server.api.service.auth.response.AuthResponse;
import com.auta.server.domain.auth.AuthTokens;

public class AuthMapper {
    public static AuthResponse toAuthResponse(AuthTokens tokens) {
        return AuthResponse.builder()
                .accessToken(tokens.accessToken())
                .build();
    }
}
