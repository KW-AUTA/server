package com.auta.server.application.service.user;


import static org.assertj.core.api.Assertions.assertThat;

import com.auta.server.IntegrationTestSupport;
import com.auta.server.adapter.out.persistence.user.UserEntity;
import com.auta.server.adapter.out.persistence.user.UserRepository;
import com.auta.server.api.service.user.response.UserResponse;
import com.auta.server.application.port.in.user.UserCreateCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest extends IntegrationTestSupport {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입 요청에 따른 유저 생성을 진행하고 유저를 반환한다.")
    @Test
    void createUser() {
        //given
        UserCreateCommand command = UserCreateCommand.builder()
                .email("test@example.com")
                .password("testPassword")
                .username("testUser")
                .build();
        //when
        UserResponse response = userService.createUser(command);

        //then
        assertThat(response).extracting("email", "username")
                .contains("test@example.com", "testUser");

        UserEntity user = userRepository.findByEmail(command.getEmail())
                .orElseThrow();

        assertThat(passwordEncoder.matches("testPassword", user.getPassword()))
                .isTrue();
    }
}