//package com.auta.server.adapter.in.security;
//
//import com.auta.server.common.token.TokenParser;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationProvider implements AuthenticationProvider {
//    private final TokenParser tokenParser;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String token = (String) authentication.getCredentials();
//
//        tokenParser.validate(token);
//        String email = tokenParser.extract(token);
//
//        User user = userManager.loadUserByUserId(userId);
//
//        return new JwtAuthenticationToken(user);
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//
//}
//
