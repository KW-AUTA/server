package com.auta.server.application.service;

import com.auta.server.application.port.in.auth.AuthCommand;
import com.auta.server.application.port.in.auth.AuthUseCase;
import com.auta.server.domain.auth.AuthTokens;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthUseCase {

    @Override
    public AuthTokens login(AuthCommand command) {
        return null;
    }

    @Override
    public void logout(String email) {
    }

    @Override
    public AuthTokens reIssue(String token) {
        return null;
    }
}
