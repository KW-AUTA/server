package com.auta.server.adapter.out.persistence.user;

import com.auta.server.application.port.out.persistence.user.UserPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.user.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPort {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BusinessException(ErrorCode.DUPLICATE_USERNAME);
        }

        UserEntity saved = userRepository.save(userMapper.toEntity(user));
        return userMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<UserEntity> userEntities = userRepository.findAllByEmail(email);

        if (userEntities.isEmpty()) {
            return Optional.empty();
        }
        if (userEntities.size() > 1) {
            throw new BusinessException(ErrorCode.DUPLICATED_DB_EMAIL);
        }
        return Optional.of(userMapper.toDomain(userEntities.get(0)));
    }

    @Override
    public void deleteByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    @Override
    public User update(User user) {
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        userEntity.updateFromDomain(user);
        return userMapper.toDomain(userEntity);
    }
}
