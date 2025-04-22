package com.auta.server.adapter.out.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.auta.server.IntegrationTestSupport;
import com.auta.server.application.port.out.auth.TokenClaimData;
import com.auta.server.domain.auth.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class JwtTokenProviderTest extends IntegrationTestSupport {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("token Claim data를 기반으로 어세스 jwt 토큰을 생성한다.")
    @Test
    void generate() {
        //given
        TokenClaimData claimData = new TokenClaimData(Map.of("email", "test@example.com"));
        TokenType type = TokenType.ACCESS;
        Date createdTime = new Date();
        //when

        String token = jwtTokenProvider.generate(claimData, type, createdTime);
        //then
        assertThat(token).isNotBlank();

        Claims parsedClaims = Jwts.parser()
                .setSigningKey(jwtTokenProvider.getSecretKey())
                .parseClaimsJws(token)
                .getBody();

        assertThat(parsedClaims.get("email")).isEqualTo("test@example.com");
        assertThat(parsedClaims.getExpiration().getTime() - parsedClaims.getIssuedAt().getTime()).isEqualTo(
                TokenType.ACCESS.getExpirationMillis());
    }
}