package com.auta.server.application.port.in.auth;

import com.auta.server.domain.auth.AuthTokens;

public interface AuthUseCase {
    AuthTokens login(AuthCommand command);

    void logout(String email);

    AuthTokens reIssue(String token);
}
