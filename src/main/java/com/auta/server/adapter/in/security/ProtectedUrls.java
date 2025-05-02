package com.auta.server.adapter.in.security;

import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class ProtectedUrls {

    public static RequestMatcher getProtectedUrls() {
        List<RequestMatcher> matchers = List.of(
                new AntPathRequestMatcher("/api/v1/users/me", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/api/v1/users", HttpMethod.PUT.name()),
                new AntPathRequestMatcher("/api/v1/users", HttpMethod.DELETE.name()),
                new AntPathRequestMatcher("/api/v1/auth/logout", HttpMethod.POST.name()),
                new AntPathRequestMatcher("/api/v1/home", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/api/v1/projects", HttpMethod.POST.name()),
                new AntPathRequestMatcher("/api/v1/projects/*", HttpMethod.PUT.name()),
                new AntPathRequestMatcher("/api/v1/projects/*", HttpMethod.DELETE.name()),
                new AntPathRequestMatcher("/api/v1/projects", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/api/v1/projects/*", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/api/v1/projects/tests", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/api/v1/projects/tests/*", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/api/v1/pages/*", HttpMethod.GET.name())
        );

        return new OrRequestMatcher(matchers);
    }
}
