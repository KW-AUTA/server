package com.auta.server.common.token;

import com.auta.server.application.port.out.auth.TokenClaimData;

public interface TokenParser {
    TokenClaimData parse(String token);
}
