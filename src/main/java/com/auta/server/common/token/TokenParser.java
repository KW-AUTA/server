package com.auta.server.common.token;

public interface TokenParser {
    TokenClaimData parse(String token);
}
