package com.auta.server.adapter.out.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieManager {

    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = CookieUtil.createRefreshCookie(refreshToken);
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void deleteRefreshCookie(HttpServletResponse response) {
        ResponseCookie refreshCookieForDelete = CookieUtil.deleteRefreshCookie();
        response.addHeader("Set-Cookie", refreshCookieForDelete.toString());
    }
}
