//package com.auta.server.adapter.in.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import jdk.jshell.spi.ExecutionControl.UserException;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.web.ErrorResponse;
//
//public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//
//    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
//        super(new AntPathRequestMatcher("/api/**", null)); //다른 요청은 못 받아와서 따로 처리 후 입력
//        this.setAuthenticationManager(authenticationManager);
//    }
//
//    @Override
//    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
//        String uri = request.getRequestURI();
//        return !PermitAllUrls.isPermitAll(uri);
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//            throws AuthenticationException, IOException {
//        try {
//            String header = request.getHeader("Authorization");
//            if (header == null || !header.startsWith("Bearer ")) {
//                throw new
//            }
//            String token = header.substring(7);
//            return this.getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
//        } catch (com.dp_annotation.common.exception.auth.AuthenticationException | TokenException | UserException e) {
//            handleException(response, e);
//            return null;
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                            FilterChain chain, Authentication authResult)
//            throws IOException, ServletException {
//        SecurityContextHolder.getContext().setAuthentication(authResult);
//        chain.doFilter(request, response);
//    }
//
//    private void handleException(HttpServletResponse response, DpAnnotationException e) throws IOException {
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json; charset=UTF-8");
//        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
//
//        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
//        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
//
//        response.getWriter().write(jsonResponse);
//    }
//}
