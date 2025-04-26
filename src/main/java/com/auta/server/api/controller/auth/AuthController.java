package com.auta.server.api.controller.auth;

import com.auta.server.api.ApiResponse;
import com.auta.server.api.controller.auth.request.AuthRequest;
import com.auta.server.api.service.auth.AuthService;
import com.auta.server.api.service.auth.response.AuthResponse;
import com.auta.server.common.cookie.CookieUtil;
import com.auta.server.domain.auth.dto.AuthTokens;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/v1/auth/login")
    public ApiResponse<AuthResponse> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        AuthTokens tokens = authService.login(request.toServiceRequest());
        ResponseCookie refreshCookie = CookieUtil.createRefreshCookie(tokens.refreshToken());
        response.addHeader("Set-Cookie", refreshCookie.toString());
        return ApiResponse.ok("로그인이 완료되었습니다.", AuthResponse.from(tokens));
    }

    @PostMapping("/api/v1/auth/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authService.logout(email);

        ResponseCookie refreshCookieForDelete = CookieUtil.deleteRefreshCookie();
        response.addHeader("Set-Cookie", refreshCookieForDelete.toString());

        return ApiResponse.ok("로그아웃이 완료되었습니다.");
    }

    @PostMapping("/api/v1/auth/reissue")
    public ApiResponse<AuthResponse> reissue(@CookieValue(value = "refreshToken", required = false) String token,
                                             HttpServletResponse response) {
        AuthTokens tokens = authService.reIssue(token);
        ResponseCookie refreshCookie = CookieUtil.createRefreshCookie(tokens.refreshToken());
        response.addHeader("Set-Cookie", refreshCookie.toString());
        return ApiResponse.ok("토큰 재발급이 완료되었습니다.", AuthResponse.from(tokens));
    }
}
