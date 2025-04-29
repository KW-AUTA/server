package com.auta.server.domain.auth;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS(1000L * 60 * 60),
    REFRESH(1000L * 60 * 60 * 24 * 14);

    private final long expirationMillis;

    TokenType(long expirationMillis) {
        this.expirationMillis = expirationMillis;
    }
}
