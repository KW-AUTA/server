package com.auta.server.adapter.out.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.auta.server.IntegrationTestSupport;
import com.auta.server.adapter.out.persistence.user.UserPersistenceAdapter;
import com.auta.server.adapter.out.persistence.user.UserRepository;
import com.auta.server.domain.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
}