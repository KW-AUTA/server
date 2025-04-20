package com.auta.server.application.service.auth.response;

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
}
