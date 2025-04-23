package com.auta.server.docs.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.api.controller.user.UserController;
import com.auta.server.api.controller.user.request.UserCreateRequest;
import com.auta.server.api.controller.user.request.UserUpdateRequest;
import com.auta.server.api.service.user.UserService;
import com.auta.server.api.service.user.request.UserCreateServiceRequest;
import com.auta.server.api.service.user.request.UserUpdateServiceRequest;
import com.auta.server.api.service.user.response.UserResponse;
import com.auta.server.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserControllerDocsTest extends RestDocsSupport {

    private final UserService userService = mock(UserService.class);

    @Override
    protected Object initController() {
        return new UserController(userService);
    }

    @DisplayName("회원가입")
    @Test
    void signUp() throws Exception {
        UserCreateRequest request = UserCreateRequest.builder()
                .email("example@example.com")
                .password("examplePassword")
                .username("exampleUser")
                .build();

        given(userService.createUser(any(UserCreateServiceRequest.class)))
                .willReturn(UserResponse.builder()
                        .id(1L)
                        .email("example@example.com")
                        .username("exampleUser")
                        .address(null)
                        .phoneNumber(null)
                        .build());

        mockMvc.perform(
                        post("/api/v1/users")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("로그인 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                        .description("유저 이름")
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
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("로그인 이메일"),
                                fieldWithPath("data.username").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("data.address").type(JsonFieldType.NULL)
                                        .description("주소"),
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.NULL)
                                        .description("전화번호")
                        )
                ));
    }

    @DisplayName("유저 정보 조회")
    @Test
    void getUser() throws Exception {
        //given
        Authentication authentication = mock(Authentication.class);
        given(authentication.getPrincipal()).willReturn("test@example.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        given(securityContext.getAuthentication()).willReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        given(userService.getUser(anyString()))
                .willReturn(UserResponse.builder()
                        .id(1L)
                        .email("example@example.com")
                        .username("exampleUser")
                        .address("example")
                        .phoneNumber("010-1234-1234")
                        .build());

        //when //then
        mockMvc.perform(
                        get("/api/v1/users/me")
                                .header("Authorization", "Bearer JwtToken")
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-get",
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
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("로그인 이메일"),
                                fieldWithPath("data.username").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("data.address").type(JsonFieldType.STRING)
                                        .description("주소"),
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING)
                                        .description("전화번호")
                        )
                ));
    }

    @DisplayName("회원정보 수정")
    @Test
    void updateUser() throws Exception {
        //given
        UserUpdateRequest request = UserUpdateRequest.builder().email("example@example.com")
                .username("exampleUser")
                .address("example")
                .phoneNumber("010-1234-1234")
                .build();

        Authentication authentication = mock(Authentication.class);
        given(authentication.getPrincipal()).willReturn("test@example.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        given(securityContext.getAuthentication()).willReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        given(userService.updateUser(any(UserUpdateServiceRequest.class), anyString()))
                .willReturn(UserResponse.builder()
                        .id(1L)
                        .email("example@example.com")
                        .username("exampleUser")
                        .address("example")
                        .phoneNumber("010-1234-1234")
                        .build());

        //when //then
        mockMvc.perform(
                        put("/api/v1/users")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer JwtToken")
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("로그인 이메일"),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                        .description("유저 이름"),
                                fieldWithPath("address").type(JsonFieldType.STRING)
                                        .description("주소"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                                        .description("전화번호")
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
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("로그인 이메일"),
                                fieldWithPath("data.username").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("data.address").type(JsonFieldType.STRING)
                                        .description("주소"),
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING)
                                        .description("전화번호")
                        )
                ));
    }

    @DisplayName("회원 탈퇴")
    @Test
    void deleteUser() throws Exception {
        //given
        Authentication authentication = mock(Authentication.class);
        given(authentication.getPrincipal()).willReturn("test@example.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        given(securityContext.getAuthentication()).willReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        doNothing().when(userService).deleteUser(anyString());

        //when //then
        mockMvc.perform(
                        delete("/api/v1/users")
                                .header("Authorization", "Bearer JwtToken")
                
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}
