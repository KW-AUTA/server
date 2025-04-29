package com.auta.server.application.port.out.auth;

public interface RefreshTokenStorePort {
    void store(String key, String token, long expirationMillis);

    void delete(String email);
}
