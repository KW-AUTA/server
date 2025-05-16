package com.auta.server.adapter.in.security;

import com.auta.server.adapter.in.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(ProtectedUrls.getProtectedUrls());
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                throw new AuthenticationCredentialsNotFoundException("Authorization 헤더가 유효하지 않습니다.");
            }

            String token = header.substring(7);
            JwtAuthenticationToken authRequest = JwtAuthenticationToken.unauthenticated(token);

            return this.getAuthenticationManager().authenticate(authRequest);

        } catch (AuthenticationException e) {
            handleException(response, e);
            return null;
        }
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
