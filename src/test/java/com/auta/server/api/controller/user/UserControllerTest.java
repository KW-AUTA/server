package com.auta.server.api.controller.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auta.server.ControllerTestSupport;
import com.auta.server.api.controller.user.request.UserCreateRequest;
import com.auta.server.api.controller.user.request.UserUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class UserControllerTest extends ControllerTestSupport {

    @DisplayName("신규 유저를 생성한다.")
    @Test
    void createUser() throws Exception {
        //given
        UserCreateRequest request = UserCreateRequest.builder()
                .email("example@example.com")
                .password("examplePassword")
                .username("example_user")
                .build();

        //when //then
        mockMvc.perform(
                        post("/api/v1/users")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("아이디에 대한 유저 정보를 조회한다.")
    @Test
    void getUser() throws Exception {
        //given
        setMockSecurityContext();
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