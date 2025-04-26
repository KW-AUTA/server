package com.auta.server.common.token;

import com.auta.server.domain.auth.dto.AuthTokens;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements TokenGenerator {
    
    @Override
    public AuthTokens generate(TokenClaimData data) {
        return null;
    }
}
