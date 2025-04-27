package com.auta.server.adapter.out.web;

import org.springframework.http.ResponseCookie;

public class CookieUtil {

    private static final String REFRESH_COOKIE_NAME = "refreshToken";
    private static final int REFRESH_COOKIE_MAX_AGE = 7 * 24 * 60 * 60;

    public static ResponseCookie createRefreshCookie(String token) {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(REFRESH_COOKIE_MAX_AGE)
                .build();
    }

    public static ResponseCookie deleteRefreshCookie() {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, "")
                .path("/")
                .maxAge(0)
                .build();
    }
}