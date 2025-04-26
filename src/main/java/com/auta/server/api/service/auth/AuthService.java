package com.auta.server.api.service.auth;

import com.auta.server.api.service.auth.request.AuthServiceRequest;
import com.auta.server.domain.auth.dto.AuthTokens;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public AuthTokens login(AuthServiceRequest request) {
        return null;
    }

    public void logout(String email) {
    }

    public AuthTokens reIssue(String token) {
        return null;
    }
}
