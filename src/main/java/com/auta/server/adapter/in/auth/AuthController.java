package com.auta.server.adapter.in.auth;

import com.auta.server.adapter.in.ApiResponse;
import com.auta.server.adapter.in.auth.request.AuthRequest;
import com.auta.server.adapter.in.auth.response.AuthResponse;
import com.auta.server.adapter.in.security.SecurityUtil;
import com.auta.server.adapter.out.web.CookieManager;
import com.auta.server.application.port.in.auth.AuthUseCase;
import com.auta.server.domain.auth.AuthTokens;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;
    private final CookieManager cookieManager;

    @PostMapping("/api/v1/auth/login")
    public ApiResponse<AuthResponse> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        AuthTokens tokens = authUseCase.login(request.toCommand());
        cookieManager.addRefreshTokenCookie(response, tokens.refreshToken());
        return ApiResponse.ok("로그인이 완료되었습니다.", AuthResponse.from(tokens));
    }

    @PostMapping("/api/v1/auth/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        String email = SecurityUtil.getCurrentPrinciple();
        authUseCase.logout(email);
        cookieManager.deleteRefreshCookie(response);

        return ApiResponse.ok("로그아웃이 완료되었습니다.");
    }

    @PostMapping("/api/v1/auth/reissue")
    public ApiResponse<AuthResponse> reissue(@CookieValue(value = "refreshToken", required = false) String token,
                                             HttpServletResponse response) {
        String email = SecurityUtil.getCurrentPrinciple();
        AuthTokens tokens = authUseCase.reIssue(email);
        cookieManager.addRefreshTokenCookie(response, tokens.refreshToken());
        return ApiResponse.ok("토큰 재발급이 완료되었습니다.", AuthResponse.from(tokens));
    }
}
