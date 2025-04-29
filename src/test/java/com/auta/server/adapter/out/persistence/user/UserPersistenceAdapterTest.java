package com.auta.server.adapter.out.persistence.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.auta.server.IntegrationTestSupport;
import com.auta.server.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class UserPersistenceAdapterTest extends IntegrationTestSupport {

    @Autowired
    private UserPersistenceAdapter userPersistenceAdapter;

    @Autowired
    private UserRepository userRepository; // 확인용으로도 필요할 수 있어

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("UserPersistenceAdapter 를 통해 유저를 저장할 수 있다.")
    @Test
    void saveUser() {
        //given
        User user = User.builder()
                .email("test@example.com")
                .password("securePassword")
                .username("testUser")
                .build();

        //when
        User savedUser = userPersistenceAdapter.save(user);

        //then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getUsername()).isEqualTo("testUser");
    }

    @DisplayName("이메일을 기반으로 optioanluser를 반환한다.")
    @Test
    void findByEmail() {
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
        Optional<User> user = userPersistenceAdapter.findByEmail(email);
        //then
        assertThat(user).isPresent();
        assertThat(user.get()).extracting("email", "username")
                .contains("test@example.com1", "testUser1");

    }

    @DisplayName("이메일을 기반으로 optioanluser를 반환한다. 이때 유저가 없으면 optional empty를 반환한다.")
    @Test
    void findByEmailEmpty() {
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

        String email = "test@example.com";
        //when
        List<UserEntity> users = userRepository.findAllByEmail(email);
        //then
        assertThat(users).isEmpty();

    }

    @DisplayName("유저를 받아서 삭제한다.")
    @Transactional
    @Test
    void deleteUser() {
        //given
        User user = User.builder()
                .email("test@example.com")
                .password("securePassword")
                .username("testUser")
                .build();

        userRepository.save(UserMapper.toEntity(user));

        //when
        userPersistenceAdapter.deleteByEmail("test@example.com");
        //then
        List<UserEntity> userEntities = userRepository.findAllByEmail("test@example.com");

        assertThat(userEntities).isEmpty();
    }
}