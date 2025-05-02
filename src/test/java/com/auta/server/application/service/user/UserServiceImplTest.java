package com.auta.server.application.service.user;


import static org.assertj.core.api.Assertions.assertThat;

import com.auta.server.IntegrationTestSupport;
import com.auta.server.adapter.out.persistence.user.UserEntity;
import com.auta.server.adapter.out.persistence.user.UserRepository;
import com.auta.server.application.port.in.user.UserCreateCommand;
import com.auta.server.application.port.in.user.UserUpdateCommand;
import com.auta.server.domain.user.User;
import java.util.List;
import java.util.Optional;
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
        User user = userService.createUser(command);

        //then
        assertThat(user).extracting("email", "username")
                .contains("test@example.com", "testUser");

        List<UserEntity> userEntities = userRepository.findAllByEmail(command.getEmail());

        assertThat(passwordEncoder.matches("testPassword", userEntities.get(0).getPassword()))
                .isTrue();
    }

    @DisplayName("이메일로 유저를 조회한다.")
    @Test
    void getUser() {
        //given
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1").password("testPassword1").username("testUser1").build();
        UserEntity userEntity2 = UserEntity.builder()
                .email("test@example.com2").password("testPassword2").username("testUser2").build();
        UserEntity userEntity3 = UserEntity.builder()
                .email("test@example.com3").password("testPassword3").username("testUser3").build();
        userRepository.saveAll(List.of(
                userEntity1, userEntity2, userEntity3
        ));

        String email = "test@example.com1";

        //when
        User user = userService.getUser(email);
        //then
        assertThat(user).extracting("email", "password", "username")
                .contains("test@example.com1", "testPassword1", "testUser1");
    }

    @DisplayName("이메일로 유저를 조회하고 command의 내용으로 수정한다..")
    @Test
    void updateUser() {
        //given
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1").password("testPassword1").username("testUser1").build();
        UserEntity userEntity2 = UserEntity.builder()
                .email("test@example.com2").password("testPassword2").username("testUser2").build();
        UserEntity userEntity3 = UserEntity.builder()
                .email("test@example.com3").password("testPassword3").username("testUser3").build();
        userRepository.saveAll(List.of(
                userEntity1, userEntity2, userEntity3
        ));

        String email = "test@example.com1";

        UserUpdateCommand command = UserUpdateCommand.builder()
                .email("test@example.com")
                .username("testUser1")
                .address("example")
                .phoneNumber("010-1234-1234")
                .build();

        //when
        User user = userService.updateUser(command, email);
        //then

        Optional<UserEntity> optionalUser = userRepository.findById(user.getId());
        UserEntity userEntity = optionalUser.get();
        assertThat(userEntity).extracting("email", "address", "username")
                .contains("test@example.com", "example", "testUser1");
    }

    @DisplayName("이메일로 유저를 삭제한다.")
    @Test
    void deleteByEmail() {
        //given
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1").password("testPassword1").username("testUser1").build();
        UserEntity userEntity2 = UserEntity.builder()
                .email("test@example.com2").password("testPassword2").username("testUser2").build();
        UserEntity userEntity3 = UserEntity.builder()
                .email("test@example.com3").password("testPassword3").username("testUser3").build();
        userRepository.saveAll(List.of(
                userEntity1, userEntity2, userEntity3
        ));

        String email = "test@example.com1";

        //when
        userService.deleteUser(email);
        //then
        List<UserEntity> userEntities = userRepository.findAllByEmail(email);

        assertThat(userEntities).isEmpty();
    }
}