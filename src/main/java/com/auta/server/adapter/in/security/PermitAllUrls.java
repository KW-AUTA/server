package com.auta.server.adapter.in.security;

import org.springframework.util.AntPathMatcher;

public class PermitAllUrls {
    public static final String[] PERMIT_ALL_URLS = {
            "/api/v1/auth/user", "/api/v1/auth/login", "/api/v1/health",
            "/api/v1/auth/reIssue", "docs/**"
    };

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    public static boolean isPermitAll(String uri) {
        for (String permitUrl : PERMIT_ALL_URLS) {
            if (pathMatcher.match(permitUrl, uri)) {
                return true;
            }
        }
        return false;
    }
}
