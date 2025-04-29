package com.auta.server.common.token;

import com.auta.server.application.port.out.auth.TokenClaimData;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenParser implements TokenParser {

    @Override
    public TokenClaimData parse(String token) {
        return null;
    }
}
