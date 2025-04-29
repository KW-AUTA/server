package com.auta.server.adapter.out.jwt;

import com.auta.server.application.port.out.auth.TokenClaimData;
import com.auta.server.application.port.out.auth.TokenProviderPort;
import com.auta.server.domain.auth.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtTokenProvider implements TokenProviderPort {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Override
    public String generate(TokenClaimData claimData, TokenType tokenType, Date createdTime) {
        Map<String, Object> claimsData = claimData.claims();
        Claims claims = Jwts.claims();
        claims.putAll(claimsData);

        Date expiry = new Date(createdTime.getTime() + tokenType.getExpirationMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(createdTime)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
