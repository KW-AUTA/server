package com.auta.server.adapter.out.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenParser {

    @Value("${jwt.secret-key}")
    private String secretKey;

    public void validate(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (SignatureException e) {
            throw new BadCredentialsException("토큰 서명 오류", e);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("토큰 만료", e);
        } catch (Exception e) {
            throw new AuthenticationServiceException("알 수 없는 토큰 오류", e);
        }
    }

    public String extract(String token, String key) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get(key, String.class);
        } catch (SignatureException e) {
            throw new BadCredentialsException("토큰 서명 오류", e);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("토큰 만료", e);
        } catch (Exception e) {
            throw new AuthenticationServiceException("알 수 없는 토큰 오류", e);
        }
    }

}
