package com.auta.server.application.service.user;

import com.auta.server.adapter.in.user.response.UserResponse;
import com.auta.server.application.port.in.user.UserCreateCommand;
import com.auta.server.application.port.in.user.UserUpdateCommand;
import com.auta.server.application.port.in.user.UserUseCase;
import com.auta.server.application.port.out.user.UserPort;
import com.auta.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserUseCase {

    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserCreateCommand command) {
        String encodedPassword = passwordEncoder.encode(command.getPassword());

        User user = User.builder()
                .email(command.getEmail())
                .password(encodedPassword)
                .username(command.getUsername())
                .build();
        return userPort.save(user);
    }

    @Override
    public UserResponse getUser(String email) {
        return null;
    }

    @Override
    public UserResponse updateUser(UserUpdateCommand command, String email) {
        return null;
    }

    @Override
    public void deleteUser(String email) {

    }
}
