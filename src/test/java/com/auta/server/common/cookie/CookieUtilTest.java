package com.auta.server.common.cookie;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

class CookieUtilTest {

    @DisplayName("토큰이 입력되면 리프레시 토큰의 쿠키를 만든다. 이때 쿠키의 이름은 refreshToken, 시간은 7일이다.")
    @Test
    void createRefreshCookie() {
        //given
        String token = "refresh-token";
        //when
        ResponseCookie refreshCookie = CookieUtil.createRefreshCookie(token);
        //then
        assertThat(refreshCookie)
                .satisfies(cookie -> {
                    assertThat(cookie.getName()).isEqualTo("refreshToken");
                    assertThat(cookie.getValue()).isEqualTo("refresh-token");
                    assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(7 * 24 * 60 * 60);
                });
    }

    @DisplayName("리프레시 토큰 쿠키의 삭제를 위한 쿠키를 만든다.")
    @Test
    void deleteRefreshCookie() {
        //given
        //when
        ResponseCookie refreshCookie = CookieUtil.deleteRefreshCookie();
        //then
        assertThat(refreshCookie)
                .satisfies(cookie -> {
                    assertThat(cookie.getName()).isEqualTo("refreshToken");
                    assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(0);
                });
    }
}