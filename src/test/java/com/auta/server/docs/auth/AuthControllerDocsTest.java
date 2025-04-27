package com.auta.server.docs.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.adapter.in.auth.AuthController;
import com.auta.server.adapter.in.auth.request.AuthRequest;
import com.auta.server.adapter.out.web.CookieManager;
import com.auta.server.application.port.in.auth.AuthUseCase;
import com.auta.server.docs.RestDocsSupport;
import com.auta.server.domain.auth.AuthTokens;
import jakarta.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class AuthControllerDocsTest extends RestDocsSupport {

    private final AuthUseCase authUseCase = mock(AuthUseCase.class);
    private final CookieManager cookieManager = new CookieManager();

    @Override
    protected Object initController() {
        return new AuthController(authUseCase, cookieManager);
    }

    @DisplayName("로그인")
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
                .andExpect(header().string("Set-Cookie", Matchers.containsString("refreshToken")))
                .andDo(document("auth-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                        .description("어세스 토큰")
                        )
                ));

    }

    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {
        //given
        setMockSecurityContext();

        //when    //then
        mockMvc.perform(
                        post("/api/v1/auth/logout")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", Matchers.allOf(
                        Matchers.containsString("refreshToken"),
                        Matchers.containsString("Max-Age=0")
                )))
                .andDo(document("auth-logout",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터")
                        )
                ));
        ;
    }

    @DisplayName("토큰 재발급")
    @Test
    void reIssue() throws Exception {
        //given
        given(authUseCase.reIssue(any())).willReturn(new AuthTokens("access-token", "refresh-token"));

        //when //then
        mockMvc.perform(
                        post("/api/v1/auth/reissue")
                                .cookie(new Cookie("refreshToken", "refresh-token"))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", Matchers.containsString("refreshToken")))
                .andDo(document("auth-reIssue",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                        .description("어세스 토큰")
                        )));

    }
}
