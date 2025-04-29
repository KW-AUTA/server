package com.auta.server.application.port.out.auth;

import com.auta.server.domain.auth.TokenType;
import java.util.Date;

public interface TokenProviderPort {
    String generate(TokenClaimData data, TokenType tokenType, Date createTime);
}
