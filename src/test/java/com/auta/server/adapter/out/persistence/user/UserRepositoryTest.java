package com.auta.server.adapter.out.persistence.user;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.auta.server.IntegrationTestSupport;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("해당 이메일을 가진 유저 리스트를 찾는다.")
    @Test
    void findAllByEmail() {
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
        List<UserEntity> users = userRepository.findAllByEmail(email);

        //then
        assertThat(users).hasSize(1);
        assertThat(users.get(0)).extracting("email", "username")
                .contains("test@example.com1", "testUser1");
    }
}