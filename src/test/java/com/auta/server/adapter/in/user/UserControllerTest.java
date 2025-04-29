package com.auta.server.adapter.in.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.ControllerTestSupport;
import com.auta.server.adapter.in.user.request.UserUpdateRequest;
import com.auta.server.application.port.in.user.UserCreateCommand;
import com.auta.server.application.port.in.user.UserUpdateCommand;
import com.auta.server.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class UserControllerTest extends ControllerTestSupport {

    @DisplayName("신규 유저를 생성한다.")
    @Test
    void createUser() throws Exception {
        //given
        UserCreateCommand request = UserCreateCommand.builder()
                .email("example@example.com")
                .password("examplePassword")
                .username("example_user")
                .build();

        given(userUseCase.createUser(any(UserCreateCommand.class)))
                .willReturn(User.builder()
                        .id(1L)
                        .email("example@example.com")
                        .username("exampleUser")
                        .address(null)
                        .phoneNumber(null)
                        .build());

        //when //then
        mockMvc.perform(
                        post("/api/v1/users")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("신규 유저를 생성 시 바인드 예외를 던진다.")
    @Test
    void createUserWithException() throws Exception {
        //given
        UserCreateCommand request = UserCreateCommand.builder()
                .email("")
                .password("examplePassword")
                .username("example_user")
                .build();

        //when //then
        mockMvc.perform(
                        post("/api/v1/users")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이메일은 필수 입력 값입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("아이디에 대한 유저 정보를 조회한다.")
    @Test
    void getUser() throws Exception {
        //given
        setMockSecurityContext();

        given(userUseCase.getUser(anyString()))
                .willReturn(User.builder()
                        .id(1L)
                        .email("example@example.com")
                        .username("exampleUser")
                        .address(null)
                        .phoneNumber(null)
                        .build());
        //when //then
        mockMvc.perform(
                        get("/api/v1/users/me")
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("아이디에 대한 유저 정보를 수정한다.")
    @Test
    void updateUser() throws Exception {
        //given
        UserUpdateRequest request = UserUpdateRequest.builder().email("example@example.com")
                .username("example_user")
                .address("example")
                .phoneNumber("010-1234-1234")
                .build();

        setMockSecurityContext();

        given(userUseCase.updateUser(any(UserUpdateCommand.class), anyString()))
                .willReturn(User.builder()
                        .id(1L)
                        .email("example@example.com")
                        .username("exampleUser")
                        .address(null)
                        .phoneNumber(null)
                        .build());
        //when //then
        mockMvc.perform(
                        put("/api/v1/users")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("아이디에 대한 유저 정보를 삭제한다.")
    @Test
    void deleteUser() throws Exception {
        //given
        setMockSecurityContext();
        //when //then
        mockMvc.perform(
                        delete("/api/v1/users")
                ).andDo(print())
                .andExpect(status().isOk());
    }
}