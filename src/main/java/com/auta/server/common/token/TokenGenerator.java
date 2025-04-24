package com.auta.server.common.token;

import com.auta.server.domain.auth.dto.AuthTokens;

public interface TokenGenerator {
    AuthTokens generate(TokenClaimData data);
}
