package com.auta.server.adapter.out.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

class CookieManagerTest {

    private final CookieManager cookieManager = new CookieManager();

    @DisplayName("리프레시 토큰 쿠키를 응답에 추가할 수 있다.")
    @Test
    void addRefreshTokenCookie() {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String refreshToken = "sample-refresh-token";

        // when
        cookieManager.addRefreshTokenCookie(response, refreshToken);

        // then
        String setCookieHeader = response.getHeader("Set-Cookie");

        assertThat(setCookieHeader).isNotNull();
        assertThat(setCookieHeader).contains(refreshToken);
        assertThat(setCookieHeader).contains("HttpOnly");
    }

    @DisplayName("리프레시 토큰 쿠키를 응답에서 없앨 시, maxAge가 0인 쿠키를 심는다.")
    @Test
    void deleteRefreshCookie() {
        //given
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        cookieManager.deleteRefreshCookie(response);

        //then
        String setCookieHeader = response.getHeader("Set-Cookie");

        assertThat(setCookieHeader).isNotNull();
        assertThat(setCookieHeader).contains("refreshToken");
        assertThat(setCookieHeader).contains("Max-Age=0");
    }
}