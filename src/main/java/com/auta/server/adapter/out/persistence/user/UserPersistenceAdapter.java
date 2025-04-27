package com.auta.server.adapter.out.persistence.user;

import com.auta.server.application.port.out.UserPort;
import com.auta.server.domain.user.User;
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
