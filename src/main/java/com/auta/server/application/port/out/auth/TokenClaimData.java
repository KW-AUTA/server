package com.auta.server.application.port.out.auth;

import java.util.Map;

public record TokenClaimData(Map<String, Object> claims) {

    public static TokenClaimData fromEmail(String email) {
        return new TokenClaimData(Map.of("email", email));
    }
}
