package com.auta.server.adapter.in.security;

import com.auta.server.adapter.in.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class RefreshTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public RefreshTokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/api/v1/auth/reissue", "POST"));
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Optional<Cookie> refreshCookie = extractRefreshTokenCookie(request);
            if (refreshCookie.isEmpty()) {
                throw new AuthenticationCredentialsNotFoundException("Refresh Token 쿠키가 없습니다.");
            }

            String refreshToken = refreshCookie.get().getValue();
            JwtAuthenticationToken authRequest = JwtAuthenticationToken.unauthenticated(refreshToken);

            return this.getAuthenticationManager().authenticate(authRequest);

        } catch (AuthenticationException e) {
            handleException(response, e);
            return null;
        }
    }

    private Optional<Cookie> extractRefreshTokenCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .findFirst();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        ApiResponse<Object> apiResponse = ApiResponse.of(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        response.getWriter().write(jsonResponse);
    }
}
