package com.auta.server.adapter.out.persistence.user;

import com.auta.server.application.port.out.user.UserPort;
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

    @Override
    public User save(User user) {
        UserEntity saved = userRepository.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<UserEntity> userEntities = userRepository.findAllByEmail(email);

        if (userEntities.isEmpty()) {
            return Optional.empty();
        }
        if (userEntities.size() > 1) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
        return Optional.of(UserMapper.toDomain(userEntities.get(0)));
    }

//    @Override
//    public Optional<User> findByEmail(String email) {
//        return userRepository.findByEmail(email)
//                .map(UserMapper::toDomain);
//    }
//
//    @Override
//    public Optional<User> findById(Long id) {
//        return userRepository.findById(id)
//                .map(UserMapper::toDomain);
//    }
//
//    @Override
//    public void deleteByEmail(String email) {
//        userRepository.deleteByEmail(email);
//    }
}
