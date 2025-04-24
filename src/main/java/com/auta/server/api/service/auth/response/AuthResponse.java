package com.auta.server.api.service.auth.response;

import com.auta.server.domain.auth.dto.AuthTokens;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;

    @Builder
    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static AuthResponse from(AuthTokens tokens) {
        return AuthResponse.builder().accessToken(tokens.accessToken()).build();
    }
}
