package com.auta.server.adapter.in.security;

import java.util.Collections;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;
    private final String principalEmail;

    private JwtAuthenticationToken(String token, String principalEmail, boolean authenticated) {
        super(authenticated ? Collections.emptyList() : null);
        this.token = token;
        this.principalEmail = principalEmail;
        setAuthenticated(authenticated);
    }

    public static JwtAuthenticationToken unauthenticated(String token) {
        return new JwtAuthenticationToken(token, null, false);
    }

    public static JwtAuthenticationToken authenticated(String principalEmail) {
        return new JwtAuthenticationToken(null, principalEmail, true);
    }


    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principalEmail;
    }
}

