package com.auta.server.application.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.auta.server.IntegrationTestSupport;
import com.auta.server.adapter.out.persistence.user.UserEntity;
import com.auta.server.adapter.out.persistence.user.UserRepository;
import com.auta.server.application.port.in.auth.AuthCommand;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.domain.auth.AuthTokens;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }


    @DisplayName("로그인을 하면 어세스 토큰과 리프레시 토큰을 담은 DTO를 반환한다.")
    @Test
    void login() {
        //given
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1")
                .password(passwordEncoder.encode("testPassword1"))
                .username("testUser1").build();
        UserEntity userEntity2 = UserEntity.builder()
                .email("test@example.com2")
                .password(passwordEncoder.encode("testPassword2"))
                .username("testUser2").build();
        UserEntity userEntity3 = UserEntity.builder()
                .email("test@example.com3")
                .password(passwordEncoder.encode("testPassword3"))
                .username("testUser3").build();

        userRepository.saveAll(List.of(
                userEntity1, userEntity2, userEntity3
        ));

        AuthCommand command = AuthCommand.builder().email("test@example.com1").password("testPassword1").build();

        //when
        AuthTokens tokens = authService.login(command);

        //then
        assertThat(tokens.accessToken()).isNotBlank();
        assertThat(tokens.refreshToken()).isNotBlank();

    }

    @DisplayName("존재하지 않는 유저의 이메일이 들어오면 예외처리한다.")
    @Test
    void loginNotFound() {
        //given
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1")
                .password(passwordEncoder.encode("testPassword1"))
                .username("testUser1").build();
        UserEntity userEntity2 = UserEntity.builder()
                .email("test@example.com2")
                .password(passwordEncoder.encode("testPassword2"))
                .username("testUser2").build();
        UserEntity userEntity3 = UserEntity.builder()
                .email("test@example.com3")
                .password(passwordEncoder.encode("testPassword3"))
                .username("testUser3").build();

        userRepository.saveAll(List.of(
                userEntity1, userEntity2, userEntity3
        ));

        AuthCommand command = AuthCommand.builder().email("none").password("testPassword1").build();

        //when //then
        assertThatThrownBy(() -> authService.login(command))
                .isInstanceOf(BusinessException.class)
                .hasMessage("존재하지 않는 사용자입니다.");
    }

    @DisplayName("올바르지 않은 비밀번호가 입력되면 예외처리한다.")
    @Test
    void loginInValidPassword() {
        //given
        UserEntity userEntity1 = UserEntity.builder()
                .email("test@example.com1")
                .password(passwordEncoder.encode("testPassword1"))
                .username("testUser1").build();
        UserEntity userEntity2 = UserEntity.builder()
                .email("test@example.com2")
                .password(passwordEncoder.encode("testPassword2"))
                .username("testUser2").build();
        UserEntity userEntity3 = UserEntity.builder()
                .email("test@example.com3")
                .password(passwordEncoder.encode("testPassword3"))
                .username("testUser3").build();

        userRepository.saveAll(List.of(
                userEntity1, userEntity2, userEntity3
        ));

        AuthCommand command = AuthCommand.builder().email("test@example.com1").password("invalid").build();

        //when //then
        assertThatThrownBy(() -> authService.login(command))
                .isInstanceOf(BusinessException.class)
                .hasMessage("비밀번호가 틀렸습니다");
    }
}