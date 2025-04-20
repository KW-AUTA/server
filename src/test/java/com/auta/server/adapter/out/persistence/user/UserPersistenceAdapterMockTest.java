package com.auta.server.adapter.out.persistence.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.auta.server.common.exception.BusinessException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserPersistenceAdapterTest2 {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserPersistenceAdapter userPersistenceAdapter;

    @Test
    @DisplayName("이메일을 기반으로 조회 시 유저가 다수면 예외를 발생시킨다.")
    void findByEmailDouble() {
        // given
        UserEntity user1 = UserEntity.builder().email("test@example.com").build();
        UserEntity user2 = UserEntity.builder().email("test@example.com").build();

        when(userRepository.findAllByEmail("test@example.com"))
                .thenReturn(List.of(user1, user2));

        // when & then
        assertThatThrownBy(() -> userPersistenceAdapter.findByEmail("test@example.com"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("db의 이메일이 중복되었습니다.");
    }
}