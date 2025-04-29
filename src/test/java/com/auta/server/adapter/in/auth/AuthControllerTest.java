package com.auta.server.adapter.in.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.ControllerTestSupport;
import com.auta.server.adapter.in.auth.request.AuthRequest;
import com.auta.server.domain.auth.AuthTokens;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class AuthControllerTest extends ControllerTestSupport {

    @DisplayName("로그인을 수행한다.")
    @Test
    void login() throws Exception {
        //given
        AuthRequest request = AuthRequest.builder().email("test@example.com").password("testPassword").build();
        given(authUseCase.login(any())).willReturn(new AuthTokens("access-token", "refresh-token"));
        
        //when //then
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        header().stringValues("Set-Cookie", Matchers.hasItem(Matchers.containsString("refreshToken"))));

    }

    @DisplayName("로그아웃을 수행한다.")
    @Test
    void logout() throws Exception {
        //given
        setMockSecurityContext();

        //when   //then
        mockMvc.perform(
                        post("/api/v1/auth/logout")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", Matchers.allOf(
                        Matchers.containsString("refreshToken"),
                        Matchers.containsString("Max-Age=0")
                )));
    }

    @DisplayName("토큰을 재발급한다.")
    @Test
    void reIssue() throws Exception {
        //given
        given(authUseCase.reIssue(any())).willReturn(new AuthTokens("access-token", "refresh-token"));

        //when //then
        mockMvc.perform(
                        post("/api/v1/auth/reissue")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", Matchers.containsString("refreshToken")));
    }
}